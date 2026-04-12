import React from "react";
import EventStepper from "./EventStepper.tsx";

interface EventFormProps {
    mode: string;
}

const EventForm: React.FC<EventFormProps> = ({mode}) => {
    return <EventStepper mode={mode}/>;
}

export default EventForm;