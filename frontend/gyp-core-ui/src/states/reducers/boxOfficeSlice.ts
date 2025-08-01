import { createSlice } from "@reduxjs/toolkit";

const boxOfficeSlice = createSlice({
    name: "boxOffice",
    initialState: {
        value: JSON.parse(localStorage.getItem("boxOfficeMode") || "false")
    },
    reducers: {
        toggleBoxOfficeMode: (state, action) => {
            state.value = action.payload;
            localStorage.setItem("boxOfficeMode", JSON.stringify(action.payload));
        }
    }
});

export const {toggleBoxOfficeMode} = boxOfficeSlice.actions;

export default boxOfficeSlice.reducer;