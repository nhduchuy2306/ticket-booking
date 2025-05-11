import { createSlice, PayloadAction } from '@reduxjs/toolkit';

export interface Todo {
    id: number;
    text: string;
    completed: boolean;
}

export interface TodoState {
    items: Todo[];
}

const initialState: TodoState = {
    items: []
};

const todoSlice = createSlice({
    name: 'todos',
    initialState,
    reducers: {
        addTodo: (state, action: PayloadAction<string>) => {
            state.items.push({
                id: Date.now(),
                text: action.payload,
                completed: false
            });
        },
        toggleTodo: (state, action: PayloadAction<number>) => {
            const todo = state.items.find(todo => todo.id === action.payload);
            if (todo) {
                todo.completed = !todo.completed;
            }
        },
        removeTodo: (state, action: PayloadAction<number>) => {
            state.items = state.items.filter(todo => todo.id !== action.payload);
        }
    }
});

export const {addTodo, toggleTodo, removeTodo} = todoSlice.actions;
export default todoSlice.reducer;
