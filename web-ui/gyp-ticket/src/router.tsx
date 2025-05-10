import { createBrowserRouter } from 'react-router-dom';
import App from "./App.tsx";
import AssignRole from "./pages/AssignRole.tsx";
import ErrorPage from "./pages/ErrorPage.tsx";

export const router = createBrowserRouter([
    {
        path: '/',
        element: <App/>,
        errorElement: <ErrorPage/>,
        children: [
            {
                path: 'test',
                element: <AssignRole/>
            },
        ],
    },
]);