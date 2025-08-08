import { createBrowserRouter } from 'react-router-dom';
import App from "./App.tsx";
import { Mode } from "./configs/Constants.ts";
import LoginPage from "./pages/auth/./LoginPage.tsx";
import RegisterPage from "./pages/auth/RegisterPage.tsx";
import CategoryPage from "./pages/category/CategoryPage.tsx";
import ConfigurationPage from "./pages/configuration/ConfigurationPage.tsx";
import ErrorPage from "./pages/error/ErrorPage.tsx";
import EventForm from "./pages/event/EventForm.tsx";
import EventPage from "./pages/event/EventPage.tsx";
import LandingPage from "./pages/landing/LandingPage.tsx";
import OrganizationPage from "./pages/organization/OrganizationPage.tsx";
import OverviewPage from "./pages/OverviewPage.tsx";
import ProfileDetailPage from "./pages/profile/ProfileDetailPage.tsx";
import ProtectRoute from "./pages/ProtectRoute.tsx";
import SaleChannelPage from "./pages/salechannel/SaleChannelPage.tsx";
import SeasonPage from "./pages/season/SeasonPage.tsx";
import SeatMapForm from "./pages/seatmap/SeatMapForm.tsx";
import SeatMapPage from "./pages/seatmap/SeatMapPage.tsx";
import TicketPage from "./pages/ticket/TicketPage.tsx";
import TicketTypePage from "./pages/tickettype/TicketTypePage.tsx";
import UserAccountForm from "./pages/useraccount/UserAccountForm.tsx";
import UserAccountPage from "./pages/useraccount/UserAccountPage.tsx";
import UserGroupPage from "./pages/usergroup/UserGroupPage.tsx";
import VenuePage from "./pages/venue/VenuePage.tsx";
import VenueMapPage from "./pages/venuemap/VenueMapPage.tsx";

const AuthServiceRouter = [
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

];

const UserServiceRouter = [
    {
        index: true,
        element: <OverviewPage/>
    },
    {
        path: 'profile-detail',
        element: <ProfileDetailPage/>
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
        path: 'organization',
        element: <OrganizationPage/>
    },
];

const EventServiceRouter = [
    {
        path: 'event',
        children: [
            {
                index: true,
                element: <EventPage/>
            },
            {
                path: 'create',
                element: <EventForm mode={Mode.CREATE.key}/>
            },
            {
                path: 'view/:id',
                element: <EventForm mode={Mode.READ_ONLY.key}/>
            },
            {
                path: 'edit/:id',
                element: <EventForm mode={Mode.EDIT.key}/>
            }
        ],
    },
    {
        path: 'venue',
        element: <VenuePage/>,
    },
    {
        path: 'venue-map',
        element: <VenueMapPage/>,
    },
    {
        path: 'ticket-type',
        element: <TicketTypePage/>,
    },
    {
        path: 'category',
        element: <CategoryPage/>,
    },
    {
        path: 'seat-map',
        children: [
            {
                index: true,
                element: <SeatMapPage/>
            },
            {
                path: 'create',
                element: <SeatMapForm mode={Mode.CREATE.key}/>
            },
            {
                path: 'view/:id',
                element: <SeatMapForm mode={Mode.READ_ONLY.key}/>
            },
            {
                path: 'edit/:id',
                element: <SeatMapForm mode={Mode.EDIT.key}/>
            }
        ]
    },
    {
        path: 'season',
        element: <SeasonPage/>,
    }
];

const TicketServiceRouter = [
    {
        path: 'ticket',
        element: <TicketPage/>
    }
];

const SaleChannelServiceRouter = [
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
];

const ConfigurationRouter = [
    {
        path: 'configuration',
        element: <ConfigurationPage/>
    }
];

const OtherRouter = [
    {
        path: 'ticket-box',
        element: <LandingPage/>
    }
];

export const router = createBrowserRouter([
    ...AuthServiceRouter,
    {
        path: '/',
        element: <ProtectRoute><App/></ProtectRoute>,
        errorElement: <ErrorPage/>,
        children: [
            ...UserServiceRouter,
            ...EventServiceRouter,
            ...TicketServiceRouter,
            ...SaleChannelServiceRouter,
            ...ConfigurationRouter
        ],
    },
    ...OtherRouter,
]);