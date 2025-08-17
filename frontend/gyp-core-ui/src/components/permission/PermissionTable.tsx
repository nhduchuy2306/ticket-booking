import { Flex, Tree, TreeDataNode, TreeProps } from "antd";
import React, { useEffect, useMemo, useState } from "react";
import { SPLITTER_CHARACTER } from "../../configs/Constants.ts";
import { ApplicationPermissionDto } from "../../models/generated/auth-service-models";

export interface PermissionTableProps {
    selectedPermissions?: React.Key[],
    allPermissions?: ApplicationPermissionDto[];
    isAdmin: boolean;
    disabled: boolean;
    onChange?: (value: React.Key[]) => void;
}

const PermissionTable: React.FC<PermissionTableProps> = ({
                                                             selectedPermissions,
                                                             allPermissions,
                                                             isAdmin,
                                                             disabled,
                                                             onChange
                                                         }) => {
    const [checkedKeys, setCheckedKeys] = useState<React.Key[]>();

    const treeData = useMemo((): TreeDataNode[] => {
        if (!allPermissions) return [];

        return allPermissions.map(perm => ({
            title: perm.name,
            key: perm.applicationId,
            children: perm.actionPermissions.map(action => ({
                title: action,
                key: `${perm.applicationId}${SPLITTER_CHARACTER}${action}`
            }))
        }));
    }, [JSON.stringify(allPermissions)]);

    useEffect(() => {
        setCheckedKeys(selectedPermissions);
    }, [selectedPermissions]);

    const onCheck: TreeProps['onCheck'] = (checkedKeysValue) => {
        setCheckedKeys(checkedKeysValue as React.Key[]);
        if (onChange) {
            onChange(checkedKeysValue as React.Key[]);
        }
    };

    if (isAdmin) {
        return null;
    }

    return (
            <Flex vertical={true}>
                <Tree
                        disabled={disabled}
                        checkable
                        onCheck={onCheck}
                        checkedKeys={checkedKeys}
                        treeData={treeData}
                        selectable={false}
                />
            </Flex>
    );
};

export default PermissionTable;