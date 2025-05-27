import { Checkbox, CheckboxChangeEvent, Flex, Input } from "antd";
import React, { useEffect, useState } from "react";
import { Mode } from "../../configs/Constants.ts";
import { UserGroupModel } from "../../models/AuthService/UserGroupModel.ts";

export interface UserGroupFormProps {
    selectedUserGroup: UserGroupModel;
    mode: string;
}

const UserGroupForm: React.FC<UserGroupFormProps> = (userGroupFormProps) => {
    const [disabled, setDisabled] = useState<boolean>(false);
    const [admin, setAdmin] = useState<boolean>(false);
    const [name, setName] = useState<string>('');
    const [description, setDescription] = useState<string>('');

    useEffect(() => {
        const {administrator, name, description} = userGroupFormProps.selectedUserGroup;
        setAdmin(administrator);
        setName(name);
        setDescription(description);
        setDisabled(userGroupFormProps.mode === Mode.READ_ONLY.key);

        if (userGroupFormProps.mode === Mode.CREATE.key) {
            setAdmin(false);
            setName('');
            setDescription('');
            setDisabled(false);
        }
    }, [userGroupFormProps]);

    const handleAdminChange = (e: CheckboxChangeEvent) => {
        console.log(e);
    }

    const handleNameChange = (e: any) => {
        console.log(e);
    }

    const handleDescriptionChange = (e: any) => {
        console.log(e);
    }

    return (
            <div style={{marginTop: "50px"}}>
                <Flex vertical={true} gap={20}>
                    <Input disabled={disabled} placeholder="Name" value={name} onChange={handleNameChange}/>
                    <Input.TextArea disabled={disabled} placeholder="Description" value={description}
                                    onChange={handleDescriptionChange}/>
                    <Checkbox disabled={disabled} checked={admin} onChange={handleAdminChange}>Administrator</Checkbox>
                </Flex>
            </div>
    );
}

export default UserGroupForm;