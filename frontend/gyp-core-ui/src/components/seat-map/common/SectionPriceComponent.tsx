import React from "react";
import { TicketTypeResponseDto } from "../../../models/generated/event-service-models";

interface SectionPriceComponentProps {
    title?: string;
    ticketTypes?: TicketTypeResponseDto[];
}

const SectionPriceComponent: React.FC<SectionPriceComponentProps> = ({title, ticketTypes}) => {
    return (
            <div>
                <p className="text-white text-center text-2xl font-bold !m-2">{title}</p>
                <div className="flex flex-col items-start justify-center gap-2 !m-4">
                    {ticketTypes?.map((ticketType) => (
                            <div key={ticketType.id} className="flex items-center justify-between w-full">
                                <div className="flex items-center gap-2">
                                    <div className="w-6 h-4 rounded" style={{backgroundColor: ticketType.color}}></div>
                                    <span className="capitalize text-white">{ticketType.name}</span>
                                </div>
                                <div className="text-white font-bold">${ticketType.price}</div>
                            </div>
                    ))}
                </div>
            </div>
    );
}

export default SectionPriceComponent;