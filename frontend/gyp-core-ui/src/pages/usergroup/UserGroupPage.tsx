import React, { useEffect, useState } from "react";
import { UserGroupPermissionModel } from "../../models/AuthService/UserGroupModel.ts";
import { UserGroupService } from "../../services/Auth/UserGroupService.ts";

const UserGroupPage: React.FC = () => {
    const [data, setData] = useState<UserGroupPermissionModel[]>([]);

    useEffect(() => {
        UserGroupService.getApplicationPermissions().then((item) => {
            console.log(item)
            setData(item)
        });
    }, []);

    return (
            <>
                <div>This is usergroup</div>
                {data.map(item => {
                    return `
                        <p>${item.applicationId}</p>
                        <p>${item.actionPermissions}</p>
                    `
                })}
            </>
    );
}

export default UserGroupPage;