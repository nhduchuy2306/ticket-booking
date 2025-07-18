import { NavigateFunction } from "react-router-dom";
import { EventResponseDto } from "../../models/generated/event-service-models";

export const handleNavigate = (navigate: NavigateFunction, path: string, entity?: EventResponseDto) => {
    if (path === '/create') {
        navigate('/event/create');
    } else if (path === '/edit') {
        navigate(`/event/edit/${entity?.id}`);
    } else if (path === '/view') {
        navigate(`/event/view/${entity?.id}`);
    } else {
        navigate('/event');
    }
};