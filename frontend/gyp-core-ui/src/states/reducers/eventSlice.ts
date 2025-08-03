import { createSlice } from "@reduxjs/toolkit";

const eventIdSlice = createSlice({
    name: 'eventId',
    initialState: {
        value: JSON.parse(localStorage.getItem('eventId') || 'null')
    },
    reducers: {
        setEventId: (state, action) => {
            state.value = action.payload;
            localStorage.setItem('eventId', JSON.stringify(action.payload));
        },
        clearEventId: (state) => {
            state.value = null;
            localStorage.removeItem('eventId');
        }
    }
});

export const {setEventId, clearEventId} = eventIdSlice.actions;