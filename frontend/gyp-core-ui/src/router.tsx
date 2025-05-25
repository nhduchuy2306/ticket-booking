import { createBrowserRouter } from 'react-router-dom';
import App from "./App.tsx";
import AssignRole from "./pages/user/AssignRole.tsx";
import LoginPage from "./pages/auth/./LoginPage.tsx";
import RegisterPage from "./pages/auth/RegisterPage.tsx";
import ErrorPage from "./pages/error/ErrorPage.tsx";
import ProtectRoute from "./pages/ProtectRoute.tsx";
import UserAccount from "./pages/user/UserAccount.tsx";

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
                path: 'test',
                element: <AssignRole/>
            },
            {
                path: 'user-account',
                element: <UserAccount/>
            },
        ],
    },
]);