import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { addTodo, removeTodo, Todo, toggleTodo } from "../../states/reducers/todoSlice.ts";
import { AppDispatch, RootState } from "../../states/store.ts";

const TodoApp: React.FC = () => {
    const [input, setInput] = useState('');
    const todos = useSelector((state: RootState) => state.todos.items);
    const dispatch = useDispatch<AppDispatch>();

    const handleAdd = () => {
        if (input.trim()) {
            dispatch(addTodo(input));
            setInput('');
        }
    };

    const renderTodoList = (todos: Todo[]) => (
            <ul>
                {todos.map(todo => (
                        <li key={todo.id}>
                            <span onClick={() => dispatch(toggleTodo(todo.id))} style={{
                                textDecoration: todo.completed ? 'line-through' : 'none',
                                cursor: 'pointer'
                            }}>{todo.text}</span>
                            <button onClick={() => dispatch(removeTodo(todo.id))}>x</button>
                        </li>
                ))}
            </ul>
    );

    return (
            <div>
                <h2>Todo List</h2>
                <input
                        value={input}
                        onChange={e => setInput(e.target.value)}
                        placeholder="Add a todo"
                />
                <button onClick={handleAdd}>Add</button>

                {renderTodoList(todos)}
            </div>
    );
};

export default TodoApp;
