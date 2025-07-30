import { createSlice } from "@reduxjs/toolkit";

const boxOfficeSlice = createSlice({
    name: "boxOffice",
    initialState: {value: false},
    reducers: {
        toggleBoxOfficeMode: (state, action) => {
            state.value = action.payload;
        }
    }
});

export const {toggleBoxOfficeMode} = boxOfficeSlice.actions;

export default boxOfficeSlice.reducer;