import { Button, Card, Transfer, Typography } from 'antd';
import React, { useState } from 'react';
import { EXAMPLE_ROLES_DATA, RoleItemModel } from "./DataTransferModel.ts";
import './data-transfer.scss';

type DataTransferProps = {
    onSubmit: (data: RoleItemModel[]) => void;
};

const DataTransfer: React.FC<DataTransferProps> = ({onSubmit}) => {
    const [targetKeys, setTargetKeys] = useState<React.Key[]>(['2']);

    const handleChange = (newTargetKeys: React.Key[]) => {
        setTargetKeys(newTargetKeys);
    };

    const handleSubmit = () => {
        const assignedRoles = EXAMPLE_ROLES_DATA.filter(role => targetKeys.includes(role.key));
        onSubmit(assignedRoles);
    };

    return (
            <div style={{padding: '24px', maxWidth: '800px', margin: '0 auto'}}>
                <Typography.Title level={3}>Role Assignment</Typography.Title>
                <Card>
                    <Transfer<RoleItemModel>
                            dataSource={EXAMPLE_ROLES_DATA}
                            targetKeys={targetKeys}
                            onChange={handleChange}
                            render={(item) => item.title}
                            rowKey={(item) => item.key}
                            listStyle={{width: '340px', height: '300px'}}
                            titles={['Available Roles', 'Assigned Roles']}
                            footer={(_, info) => {
                                return info?.direction === 'left' ? 'Available roles' : 'Assigned roles';
                            }}
                    />
                    <div style={{marginTop: '16px', textAlign: 'right'}}>
                        <Button type="primary" onClick={handleSubmit}>
                            Save Roles
                        </Button>
                    </div>
                </Card>
            </div>
    );
}

export default DataTransfer;