import { Switch } from "antd";
import React from "react";

export interface SeatMapEditorFieldProps {
    label: string;
    value: string;
    onChange: (value: string) => void;
    error?: string;
    type?: "text" | "number" | "switch";
}

const SeatMapEditorField: React.FC<SeatMapEditorFieldProps> = ({label, value, onChange, error, type = "text"}) => {
    return (
            <>
                {type === "switch"
                        ? <label className="flex items-center gap-3 text-sm text-slate-900">
                            <span>{label}</span>
                            <Switch checked={value === "true"}
                                    onChange={(checked) => onChange(checked.toString())}/>
                        </label>
                        : <label className="flex flex-col gap-1.5 text-sm text-slate-900">
                            <span>{label}</span>
                            <input type={type}
                                   value={value}
                                   onChange={(event) => onChange(event.target.value)}
                                   className={`w-full border bg-white px-3! py-2! text-sm outline-none transition focus:border-slate-900 rounded-xl ${error ? "border-red-300" : "border-slate-300"}`}
                                   aria-invalid={Boolean(error)}/>
                            {error ? <span className="text-xs text-red-600">{error}</span> : null}
                        </label>
                }
            </>
    );
};

export default SeatMapEditorField;
