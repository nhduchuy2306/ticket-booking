import type { MenuProps } from 'antd';
import { Layout, Menu } from 'antd';
import { SelectInfo } from "rc-menu/lib/interface";
import React, { useState } from 'react';
import { AiOutlineUser, AiOutlineUsergroupAdd } from "react-icons/ai";
import { BsCalendar3 } from "react-icons/bs";
import { CiLocationOn, CiShop } from "react-icons/ci";
import { IoIosLogOut } from "react-icons/io";
import { PiSeat } from "react-icons/pi";
import { Outlet, useNavigate } from "react-router-dom";
import "./app.scss";

const {Content, Sider} = Layout;

type MenuItem = Required<MenuProps>['items'][number];

const siderStyle: React.CSSProperties = {
    overflow: 'auto',
    height: '100vh',
    position: 'sticky',
    insetInlineStart: 0,
    top: 0,
    bottom: 0,
    scrollbarWidth: 'thin',
    scrollbarGutter: 'stable',
};

function getItem(label: React.ReactNode, key: React.Key, icon?: React.ReactNode, children?: MenuItem[]): MenuItem {
    return {key, icon, children, label} as MenuItem;
}

const items: MenuItem[] = [
    getItem('User Account', 'user-account', <AiOutlineUser/>, [
        getItem('User Account', 'user-account', <AiOutlineUser/>),
        getItem('User Group', 'user-group', <AiOutlineUsergroupAdd/>)
    ]),
    getItem('Event', 'event', <BsCalendar3/>),
    getItem('Venue', 'venue', <CiLocationOn/>),
    getItem('Seat Map', 'seat-map', <PiSeat/>),
    getItem('Sale', 'sale', <CiShop/>),
    getItem('Logout', 'logout', <IoIosLogOut/>)
];

const App: React.FC = () => {
    const navigate = useNavigate();
    const [key, setKey] = useState<string>('user-account');

    const handleMenuItemSelect = (info: SelectInfo) => {
        if (info.key === 'logout') {
            localStorage.removeItem("token");
            navigate('/login');
        } else {
            navigate(`/${info.key}`)
            setKey(info.key);
        }
    }

    return (
            <Layout hasSider className="layout-container">
                <Sider collapsible style={siderStyle} width={280} theme="light">
                    <div className="logo-vertical"/>
                    <Menu
                            style={{height: '100%', width: '100%'}}
                            theme="light"
                            mode="inline"
                            defaultSelectedKeys={['user-account']}
                            selectedKeys={[key]}
                            items={items}
                            onSelect={handleMenuItemSelect}
                    />
                </Sider>
                <Layout>
                    <Content style={{margin: '24px 16px 0', overflow: 'auto'}}>
                        <Outlet/>
                    </Content>
                </Layout>
            </Layout>
    );
};

export default App;