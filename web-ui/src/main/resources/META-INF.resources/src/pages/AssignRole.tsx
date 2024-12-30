import React, { useState } from "react";
import DataTransfer from "../components/DataTransfer/DataTransfer.tsx";
import { RoleItemModel } from "../components/DataTransfer/DataTransferModel.ts";

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
            </div>
    );
};

export default AssignRole;