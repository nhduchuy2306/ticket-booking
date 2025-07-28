import { Breadcrumb, Layout, Menu } from 'antd';
import { SelectInfo } from "rc-menu/lib/interface";
import React, { useEffect, useState } from 'react';
import { AiOutlineUser, AiOutlineUsergroupAdd } from "react-icons/ai";
import { BiBuilding, BiCategory, BiMoney } from "react-icons/bi";
import { BsCalendar3, BsTicket } from "react-icons/bs";
import { CiLocationOn, CiSettings, CiShop } from "react-icons/ci";
import { GrShareOption } from "react-icons/gr";
import { IoIosLogOut } from "react-icons/io";
import { LiaFirstOrder } from "react-icons/lia";
import { PiSeat } from "react-icons/pi";
import { Outlet, useLocation, useNavigate } from "react-router-dom";
import "./app.scss";
import ProfilePage from "./pages/profile/ProfilePage.tsx";
import { findMenuPath, getItem, getLabelByKey, MenuItem } from "./services/AppService.ts";

const {Content, Sider} = Layout;

const menuItems: MenuItem[] = [
    getItem('User Service', 'user-service', <AiOutlineUser/>, [
        getItem('User Account', 'user-account', <AiOutlineUser/>),
        getItem('User Group', 'user-group', <AiOutlineUsergroupAdd/>),
        getItem('Organization', 'organization', <BiBuilding/>),
    ]),
    getItem('Event Service', 'event-service', <BsCalendar3/>, [
        getItem('Event', 'event', <BsCalendar3/>),
        getItem('Category', 'category', <BiCategory/>),
        getItem('Venue', 'venue', <CiLocationOn/>),
        getItem('Season', 'season', <GrShareOption/>),
        getItem('Seat Map', 'seat-map', <PiSeat/>),
        getItem('Ticket Type', 'ticket-type', <BiCategory/>),
    ]),
    getItem('Ticket Service', 'ticket-service', <BsTicket/>, [
        getItem('Ticket', 'ticket', <BsTicket/>),
        getItem('Order', 'order', <BiMoney/>),
    ]),
    getItem('Sale Channel Service', 'sale-channel-service', <LiaFirstOrder/>, [
        getItem('Sale Channel', 'sale-channel', <CiShop/>),
        getItem('Sale Channel Type', 'sale-channel-type', <CiShop/>),
    ]),
    getItem('Configuration Service', 'configuration-service', <CiSettings/>, [
        getItem('Configuration', 'configuration', <CiSettings/>),
    ]),
    getItem('Logout', 'logout', <IoIosLogOut/>)
];

const allItems: MenuItem[] = [
    {
        key: 'profile-detail',
        label: 'Profile Detail',
    },
    ...menuItems,
]

const App: React.FC = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [selectedKey, setSelectedKey] = useState<string>('');
    const [openKeys, setOpenKeys] = useState<string[]>([]);

    useEffect(() => {
        const pathParts = location.pathname.split('/');
        const path = pathParts.length > 1 ? pathParts[1] : '';
        setSelectedKey(path);

        const menuPath = findMenuPath(menuItems, path);
        if (menuPath && menuPath.length > 1) {
            setOpenKeys(menuPath.slice(0, -1));
        }
    }, [location.pathname]);

    const menuPath = findMenuPath(allItems, selectedKey);
    const breadcrumbItems = menuPath
            ? menuPath.map(key => getLabelByKey(key, allItems)).filter(Boolean)
            : [selectedKey.replace('-', ' ')];

    const breadCrumbItems = [
        {
            title: 'Home',
            key: 'home',
        },
        ...breadcrumbItems.map((title, idx) => ({
            title: title,
            key: `breadcrumb-${idx}`,
        }))
    ];

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
                    <ProfilePage/>
                    <Menu
                            className="w-full h-full"
                            theme="light"
                            mode="inline"
                            selectedKeys={[selectedKey]}
                            openKeys={openKeys}
                            onOpenChange={(keys) => setOpenKeys(keys)}
                            items={menuItems}
                            onSelect={handleMenuItemSelect}
                    />
                </Sider>
                <Layout className="bg-white">
                    <Content className="mt-[24px]! mr-[16px]! ml-[16px]! mb-0!">
                        <Breadcrumb items={breadCrumbItems}/>
                        <Outlet/>
                    </Content>
                </Layout>
            </Layout>
    );
};

export default App;