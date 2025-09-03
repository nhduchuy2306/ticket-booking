import { Tabs, TabsProps } from "antd";
import React from "react";
import 'antd/dist/reset.css';
import SeatMapEditorTab from "./tabs/SeatMapEditorTab.tsx";
import SeatMapFormTab from "./tabs/SeatMapFormTab.tsx";

export interface SeatMapDetailFormProps {
    mode: string;
}

const SeatMapDetailForm: React.FC<SeatMapDetailFormProps> = ({mode}) => {
    const items: TabsProps['items'] = [
        {
            key: 'seat-map-details',
            label: 'Seat Map Details',
            children: <SeatMapFormTab mode={mode}/>,
        },
        {
            key: 'seat-map-editor',
            label: 'Seat Map Editor',
            children: <SeatMapEditorTab mode={mode}/>,
        },
        {
            key: 'seat-map-viewer',
            label: 'Seat Map Viewer',
            children: <SeatMapEditorTab mode={mode}/>,
        },
    ];

    return (
            <Tabs defaultActiveKey="1" type="card" items={items}/>
    );
}

export default SeatMapDetailForm;