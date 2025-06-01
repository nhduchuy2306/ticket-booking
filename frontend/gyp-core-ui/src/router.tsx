import { createBrowserRouter } from 'react-router-dom';
import App from "./App.tsx";
import { Mode } from "./configs/Constants.ts";
import LoginPage from "./pages/auth/./LoginPage.tsx";
import RegisterPage from "./pages/auth/RegisterPage.tsx";
import ConfigurationPage from "./pages/configuration/ConfigurationPage.tsx";
import ErrorPage from "./pages/error/ErrorPage.tsx";
import EventPage from "./pages/event/EventPage.tsx";
import OverviewPage from "./pages/OverviewPage.tsx";
import ProtectRoute from "./pages/ProtectRoute.tsx";
import SaleChannelPage from "./pages/salechannel/SaleChannelPage.tsx";
import SeatMapPage from "./pages/seatmap/SeatMapPage.tsx";
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
                        path: 'new',
                        element: <UserAccountForm mode={Mode.CREATE.key}/>
                    },
                    {
                        path: ':id',
                        element: <UserAccountForm mode={Mode.EDIT.key}/>
                    }
                ]
            },
            {
                path: 'user-group',
                element: <UserGroupPage/>
            },
            {
                path: 'event',
                element: <EventPage/>
            },
            {
                path: 'venue',
                element: <VenuePage/>
            },
            {
                path: 'sale-channel',
                element: <SaleChannelPage/>
            },
            {
                path: 'seat-map',
                element: <SeatMapPage/>
            },
            {
                path: 'configuration',
                element: <ConfigurationPage/>
            }
        ],
    },
]);