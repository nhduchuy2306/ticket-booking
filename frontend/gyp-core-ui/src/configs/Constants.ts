export const BASE_URL: string = "localhost:9999/";

export const SPLITTER_CHARACTER = '\u25CF';

export const Mode = Object.freeze({
    READ_ONLY: {key: "READ_ONLY", value: "read_only"},
    EDIT: {key: "EDIT", value: "edit"},
    CREATE: {key: "CREATE", value: "create"}
});

export const ThemeColors = Object.freeze({
    SELECTED: "#fafafa",
    PRIMARY: "#1E88E5",
    SECONDARY: "#F5F5F5",
    SUCCESS: "#4CAF50",
    ERROR: "#F44336",
    WARNING: "#FF9800",
    INFO: "#2196F3",
    LIGHT: "#FFFFFF",
    DARK: "#000000",
    BACKGROUND: "#F0F0F0",
    TEXT: "#333333",
    BORDER: "#E0E0E0",
    HIGHLIGHT: "#FFEB3B",
    SHADOW: "#BDBDBD",
    TRANSPARENT: "rgba(0, 0, 0, 0.5)",
    DISABLED: "#BDBDBD",
    ACTIVE: "#1E88E5",
    INACTIVE: "#9E9E9E",
});