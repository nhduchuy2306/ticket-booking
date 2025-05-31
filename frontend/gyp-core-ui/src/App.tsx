import { Layout, Menu } from 'antd';
import { SelectInfo } from "rc-menu/lib/interface";
import React, { useEffect, useState } from 'react';
import { AiOutlineUser, AiOutlineUsergroupAdd } from "react-icons/ai";
import { BsCalendar3 } from "react-icons/bs";
import { CiLocationOn, CiShop } from "react-icons/ci";
import { IoIosLogOut } from "react-icons/io";
import { PiSeat } from "react-icons/pi";
import { Outlet, useLocation, useNavigate } from "react-router-dom";
import "./app.scss";
import { findMenuPath, getItem, MenuItem } from "./services/AppService.ts";

const {Content, Sider} = Layout;

const items: MenuItem[] = [
    getItem('User', 'user', <AiOutlineUser/>, [
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
    const location = useLocation();
    const [selectedKey, setSelectedKey] = useState<string>('');
    const [openKeys, setOpenKeys] = useState<string[]>([]);

    useEffect(() => {
        const path = location.pathname.substring(1); // remove the leading "/"
        setSelectedKey(path);

        const menuPath = findMenuPath(items, path);
        if (menuPath && menuPath.length > 1) {
            setOpenKeys(menuPath.slice(0, -1));
        }
    }, [location.pathname]);

    const handleMenuItemSelect = (info: SelectInfo) => {
        if (info.key === 'logout') {
            localStorage.removeItem("token");
            navigate('/login');
        } else {
            navigate(`/${info.key}`)
        }
    }

    return (
            <Layout hasSider className="layout-container">
                <Sider collapsible className="app-side" width={280} theme="light">
                    <div className="logo-vertical"/>
                    <Menu
                            style={{height: '100%', width: '100%'}}
                            theme="light"
                            mode="inline"
                            selectedKeys={[selectedKey]}
                            openKeys={openKeys}
                            onOpenChange={(keys) => setOpenKeys(keys)}
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