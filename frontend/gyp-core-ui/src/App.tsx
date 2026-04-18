import { Breadcrumb, Layout, Menu } from 'antd';
import { SelectInfo } from "rc-menu/lib/interface";
import React, { useEffect, useState } from 'react';
import { AiOutlineUser, AiOutlineUsergroupAdd } from "react-icons/ai";
import { BiBuilding, BiCategory, BiImage, BiMoney } from "react-icons/bi";
import { BsCalendar3, BsTicket } from "react-icons/bs";
import { CiLocationOn, CiSettings, CiShop } from "react-icons/ci";
import { GrShareOption } from "react-icons/gr";
import { IoIosLogOut } from "react-icons/io";
import { LiaFirstOrder } from "react-icons/lia";
import { PiSeat } from "react-icons/pi";
import { useSelector } from "react-redux";
import { Outlet, useLocation, useNavigate } from "react-router-dom";
import ProfilePage from "./pages/profile/ProfilePage.tsx";
import { filterMenu, findMenuPath, getItem, getLabelByKey, MenuItem } from "./services/AppService.ts";
import { UserAccountService } from "./services/Auth/UserAccountService.ts";
import { IamService } from "./services/Iam/IamService.ts";
import { RootState } from "./states/store.ts";

const {Content, Sider} = Layout;

const menuItems: MenuItem[] = [
    getItem('User Service', 'user-service', 'user-service', <AiOutlineUser/>, [
        getItem('User Account', 'user-account', 'app.user.account', <AiOutlineUser/>),
        getItem('User Group', 'user-group', 'app.user.group', <AiOutlineUsergroupAdd/>),
        getItem('Organization', 'organization', 'app.organization', <BiBuilding/>),
    ]),
    getItem('Event Service', 'event-service', 'event-service', <BsCalendar3/>, [
        getItem('Event', 'event', 'app.event', <BsCalendar3/>),
        getItem('Event Image', 'event-image', 'app.event.image', <BiImage/>),
        getItem('Category', 'category', 'app.category', <BiCategory/>),
        getItem('Venue', 'venue', 'app.venue', <CiLocationOn/>),
        getItem('Venue Map', 'venue-map', 'app.venue.map', <CiLocationOn/>),
        getItem('Season', 'season', 'app.season', <GrShareOption/>),
        getItem('Seat Map', 'seat-map', 'app.seat.map', <PiSeat/>),
        getItem('Ticket Type', 'ticket-type', 'app.ticket.type', <BiCategory/>),
    ]),
    getItem('Ticket Service', 'ticket-service', 'ticket-service', <BsTicket/>, [
        getItem('Ticket', 'ticket', 'app.ticket', <BsTicket/>),
        getItem('Order', 'order', 'app.order', <BiMoney/>),
    ]),
    getItem('Sale Channel Service', 'sale-channel-service', 'sale-channel-service', <LiaFirstOrder/>, [
        getItem('Sale Channel', 'sale-channel', 'app.sale.channel', <CiShop/>),
        getItem('Sale Channel Type', 'sale-channel-type', 'app.sale.channel.type', <CiShop/>),
    ]),
    getItem('Configuration Service', 'configuration-service', 'configuration-service', <CiSettings/>, [
        getItem('Configuration', 'configuration', 'app.configuration', <CiSettings/>),
    ]),
    getItem('Logout', 'logout', 'logout', <IoIosLogOut/>)
];

const boxOfficeMenuItems: MenuItem[] = [
    getItem('Box Office', 'box-office', 'box-office', <BiMoney/>, [
        getItem('Box Office Dashboard', 'box-office-dashboard', 'box-office-dashboard', <BiMoney/>),
        getItem('Box Office Report', 'box-office-report', 'box-office-report', <BiMoney/>),
        getItem('Box Office Settings', 'box-office-settings', 'box-office-settings', <CiSettings/>),
        getItem('Box Office Sale Ticket', 'box-office-sale-ticket', 'box-office-sale-ticket', <BsTicket/>),
    ]),
];

const allItems: MenuItem[] = [
    {
        key: 'profile-detail',
        label: 'Profile Detail',
    },
    ...menuItems,
    ...boxOfficeMenuItems
];

const siderStyle: React.CSSProperties = {
    overflow: 'auto',
    position: 'sticky',
    insetInlineStart: 0,
    top: 0,
    bottom: 0,
    scrollbarWidth: 'thin',
    scrollbarGutter: 'stable'
};

const DEFAULT_PATH = 'user-account';

const App: React.FC = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [selectedKey, setSelectedKey] = useState<string>('');
    const [openKeys, setOpenKeys] = useState<string[]>([]);
    const [collapsed, setCollapsed] = useState(false);
    const boxOfficeMode = useSelector((state: RootState) => state.boxOffice.value);
    const userId = IamService.getUserId();
    const [filteredMenu, setFilteredMenu] = useState<MenuItem[]>([]);

    useEffect(() => {
        const pathParts = location.pathname.split('/');
        let path = pathParts.length > 1 ? pathParts[1] : '';
        if (boxOfficeMode && boxOfficeMenuItems.every(item => item.key !== path)) {
            navigate("/");
        } else if (path === '') {
            path = DEFAULT_PATH;
            navigate(`/${DEFAULT_PATH}`);
        }
        setSelectedKey(path);

        const menuPath = findMenuPath(menuItems, path);
        if (menuPath && menuPath.length > 1) {
            setOpenKeys(menuPath.slice(0, -1));
        }
    }, [boxOfficeMode, location.pathname, navigate]);

    useEffect(() => {
        (async () => {
            const currentUserAccount = await UserAccountService.getUserAccountById(userId || '');
            const userGroupList = currentUserAccount.userGroupList;
            const permissions = new Map<string, string[]>();
            let isAdministrator = false;
            userGroupList.forEach(group => {
                isAdministrator = group.administrator;
                if (isAdministrator) {
                    return;
                }
                group.userGroupPermissions?.permissionItems.map(item => {
                    if (permissions.get(item.applicationId)) {
                        const existingActions = permissions.get(item.applicationId);
                        permissions.set(item.applicationId, {...existingActions, ...item.actions});
                    } else {
                        permissions.set(item.applicationId, item.actions);
                    }
                });
            });

            const filtered = filterMenu(isAdministrator, menuItems, permissions);
            setFilteredMenu(filtered);
        })();
    }, [userId]);

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
            IamService.redirectToLogin();
        } else {
            navigate(`/${info.key}`)
        }
    }

    return (
            <Layout hasSider>
                <Sider collapsible
                       collapsed={collapsed}
                       onCollapse={(value) => setCollapsed(value)}
                       className="app-side !overflow-hidden"
                       style={siderStyle}
                       width={280}
                       theme="light">
                    <ProfilePage collapsed={collapsed}/>
                    <Menu
                            className="w-full h-full"
                            theme="light"
                            mode="inline"
                            selectedKeys={[selectedKey]}
                            openKeys={openKeys}
                            onOpenChange={(keys) => setOpenKeys(keys)}
                            items={boxOfficeMode ? boxOfficeMenuItems : filteredMenu}
                            onSelect={handleMenuItemSelect}
                    />
                </Sider>
                <Layout className="h-[100vh] max-h-[100vh]">
                    <Content className="!m-5 p-0 !overflow-hidden">
                        <Breadcrumb items={breadCrumbItems}/>
                        <Outlet/>
                    </Content>
                </Layout>
            </Layout>
    );
};

export default App;