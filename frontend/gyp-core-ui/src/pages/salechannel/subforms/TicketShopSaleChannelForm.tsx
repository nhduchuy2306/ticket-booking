import { Form, Input } from "antd";
import React from "react";

const TicketShopSaleChannelForm: React.FC = () => {
    return (
            <>
                <Form.Item
                        name={["saleChannelConfig", "siteUrl"]}
                        label="Site URL"
                        rules={[{required: true, message: 'Please enter the site URL'}]}
                >
                    <Input placeholder="Enter the site URL"/>
                </Form.Item>
                <Form.Item
                        name={["saleChannelConfig", "siteTitle"]}
                        label="Site Title"
                        rules={[{required: true, message: 'Please enter the site Title'}]}
                >
                    <Input placeholder="Enter the site Title"/>
                </Form.Item>
            </>
    )
}

export default TicketShopSaleChannelForm;