import React, { useState } from "react";
import DataTransfer from "../components/DataTransfer/DataTransfer.tsx";
import { RoleItemModel } from "../components/DataTransfer/DataTransferModel.ts";
import SeatGridEditor from "../components/SeatMap/SeatGridEditor.tsx";
import SeatMapViewer from "../components/SeatMap/SeatMapViewer.tsx";

const AssignRole: React.FC = () => {
    const [assignedRoles, setAssignedRoles] = useState<RoleItemModel[]>([]);

    const handleSubmit = (data: RoleItemModel[]) => {
        setAssignedRoles(data);
        console.log('Roles stored in state:', data);
    };

    return (
            <div>
                <DataTransfer onSubmit={handleSubmit}/>
                <pre>{assignedRoles.length > 0 ? JSON.stringify(assignedRoles, null, 2) : ''}</pre>
                <SeatMapViewer eventId={"1"}/>
                <SeatGridEditor/>
            </div>
    );
};

export default AssignRole;