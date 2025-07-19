import { Tabs, TabsProps } from "antd";
import React from "react";
import 'antd/dist/reset.css';
import SeatMapFormTab from "./tabs/SeatMapFormTab.tsx";
import SeatMapRendererTab from "./tabs/SeatMapRendererTab.tsx";

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
            key: 'seat-map-layout',
            label: 'Seat Map Layout',
            children: <SeatMapRendererTab mode={mode}/>,
        },
    ];

    return (
            <Tabs defaultActiveKey="1" type="card" items={items}/>
    );
}

export default SeatMapDetailForm;