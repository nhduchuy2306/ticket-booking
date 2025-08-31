import React, { useEffect } from "react";
import { useRoutes } from "react-router-dom";
import LandingPage from "./gyp-pages/landing/LandingPage.tsx";
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
            if(routeResponse) {
                const dynamicRoutes = routeResponse.map((channel) => {
                    if(channel.saleChannelConfig?.type === 'TICKET_SHOP') {
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
            index: true,
            element: <LandingPage/>,
        },
        ...routes,
        {
            path: "dashboard",
            element: <div>Dashboard Page - Coming Soon!</div>,
        }
    ]);
}

export default GypApp;