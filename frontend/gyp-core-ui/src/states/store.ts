import { configureStore } from '@reduxjs/toolkit';
import counterSlice from "./reducers/counterSlice.ts";
import todoSlice from "./reducers/todoSlice.ts";

export const store = configureStore({
    reducer: {
        counter: counterSlice,
        todos: todoSlice
    }
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
