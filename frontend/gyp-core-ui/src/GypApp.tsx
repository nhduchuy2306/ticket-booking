import React, { useEffect } from "react";
import { useRoutes } from "react-router-dom";
import EventDetailPage from "./gyp-pages/events/EventDetailPage.tsx";
import GypRootLayout from "./gyp-pages/GypRootLayout.tsx";
import LandingPage from "./gyp-pages/landing/LandingPage.tsx";
import EventOrderPage from "./gyp-pages/orders/EventOrderPage.tsx";
import EventSeatMap from "./gyp-pages/seat-maps/EventSeatMap.tsx";
import { TicketShopSaleChannelConfigDto } from "./models/generated/sale-channel-service-models";
import { SaleChannelService } from "./services/SaleChannel/SaleChannelService.ts";

interface Route {
    path: string;
    element: React.ReactNode;
}

const GypApp: React.FC = () => {
    const [routes, setRoutes] = React.useState<Route[]>([]);

    useEffect(() => {
        const fetchRoutes = async () => {
            const routeResponse = await SaleChannelService.getActiveSaleChannels();
            console.log(routeResponse);
            if (routeResponse) {
                const dynamicRoutes = routeResponse.map((channel) => {
                    if (channel.saleChannelConfig?.type === 'TICKET_SHOP') {
                        return {
                            path: (channel.saleChannelConfig as TicketShopSaleChannelConfigDto).siteUrl,
                            element: <div>Welcome to {channel.name} Ticket Shop!</div>,
                        }
                    }
                });
                setRoutes(dynamicRoutes.filter(route => route !== undefined) as Route[]);
            }
        }
        void fetchRoutes();
    }, []);

    return useRoutes([
        {
            path: "/",
            element: <GypRootLayout/>,
            children: [
                {
                    index: true,
                    element: <LandingPage/>,
                },
                {
                    path: "events/:id",
                    element: <EventDetailPage/>,
                },
                {
                    path: "events/:id/orders",
                    element: <EventOrderPage/>,
                },
                ...routes,
            ]
        },
        {
            path: "events/:id/choose-seats",
            element: <EventSeatMap/>,
        },
    ]);
}

export default GypApp;