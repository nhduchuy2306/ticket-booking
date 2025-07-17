import { createBrowserRouter } from 'react-router-dom';
import App from "./App.tsx";
import { Mode } from "./configs/Constants.ts";
import LoginPage from "./pages/auth/./LoginPage.tsx";
import RegisterPage from "./pages/auth/RegisterPage.tsx";
import CategoryPage from "./pages/category/CategoryPage.tsx";
import ConfigurationPage from "./pages/configuration/ConfigurationPage.tsx";
import ErrorPage from "./pages/error/ErrorPage.tsx";
import EventPage from "./pages/event/EventPage.tsx";
import EventPageForm from "./pages/event/EventPageForm.tsx";
import OverviewPage from "./pages/OverviewPage.tsx";
import ProtectRoute from "./pages/ProtectRoute.tsx";
import SaleChannelPage from "./pages/salechannel/SaleChannelPage.tsx";
import SeatMapPage from "./pages/seatmap/SeatMapPage.tsx";
import TicketTypePage from "./pages/tickettype/TicketTypePage.tsx";
import UserAccountForm from "./pages/useraccount/UserAccountForm.tsx";
import UserAccountPage from "./pages/useraccount/UserAccountPage.tsx";
import UserGroupPage from "./pages/usergroup/UserGroupPage.tsx";
import VenuePage from "./pages/venue/VenuePage.tsx";

export const router = createBrowserRouter([
    {
        path: 'login',
        element: <LoginPage/>,
        errorElement: <ErrorPage/>,
    },
    {
        path: 'sign-up',
        element: <RegisterPage/>,
        errorElement: <ErrorPage/>,
    },
    {
        path: '/',
        element: <ProtectRoute><App/></ProtectRoute>,
        errorElement: <ErrorPage/>,
        children: [
            {
                index: true,
                element: <OverviewPage/>
            },
            {
                path: 'user-account',
                children: [
                    {
                        index: true,
                        element: <UserAccountPage/>
                    },
                    {
                        path: 'create',
                        element: <UserAccountForm mode={Mode.CREATE.key}/>
                    },
                    {
                        path: 'view/:id',
                        element: <UserAccountForm mode={Mode.READ_ONLY.key}/>
                    },
                    {
                        path: 'edit/:id',
                        element: <UserAccountForm mode={Mode.EDIT.key}/>
                    }
                ]
            },
            {
                path: 'user-group',
                element: <UserGroupPage/>
            },
            {
                path: 'category',
                element: <CategoryPage/>,
            },
            {
                path: 'event',
                children: [
                    {
                        index: true,
                        element: <EventPage/>
                    },
                    {
                        path: 'new',
                        element: <EventPageForm mode={Mode.CREATE.key}/>
                    },
                    {
                        path: ':id',
                        element: <EventPageForm mode={Mode.EDIT.key}/>
                    }
                ],
            },
            {
                path: 'venue',
                element: <VenuePage/>,
            },
            {
                path: 'ticket-type',
                element: <TicketTypePage/>,
            },
            {
                path: 'sale-channel',
                element: <SaleChannelPage/>,
                children: [
                    {
                        index: true,
                        element: <SaleChannelPage/>
                    },
                    {
                        path: 'new',
                        element: <SaleChannelPage/>
                    },
                    {
                        path: ':id',
                        element: <SaleChannelPage/>
                    }
                ]
            },
            {
                path: 'seat-map',
                element: <SeatMapPage/>,
                children: [
                    {
                        index: true,
                        element: <SeatMapPage/>
                    },
                    {
                        path: 'new',
                        element: <SeatMapPage/>
                    },
                    {
                        path: ':id',
                        element: <SeatMapPage/>
                    }
                ]
            },
            {
                path: 'configuration',
                element: <ConfigurationPage/>
            }
        ],
    },
]);