import React, { useEffect, useState } from "react";
import { useRoutes } from "react-router-dom";
import LoginPage from "./gyp-pages/auths/LoginPage.tsx";
import OAuth2CallbackPage from "./gyp-pages/auths/OAuth2CallbackPage.tsx";
import RegisterPage from "./gyp-pages/auths/RegisterPage.tsx";
import EventDetailPage from "./gyp-pages/events/EventDetailPage.tsx";
import { GypPageContext } from "./gyp-pages/GypPageContext.tsx";
import GypRootLayout from "./gyp-pages/GypRootLayout.tsx";
import LandingPage from "./gyp-pages/landing/LandingPage.tsx";
import EventOrderPage from "./gyp-pages/orders/EventOrderPage.tsx";
import PaymentFailurePage from "./gyp-pages/orders/payment-status/PaymentFailurePage.tsx";
import PaymentSuccessPage from "./gyp-pages/orders/payment-status/PaymentSuccessPage.tsx";
import OrganizationRegisterPage from "./pages/organization/OrganizationRegisterPage.tsx";
import WaitingRoomPage from "./gyp-pages/waiting-room/WaitingRoomPage.tsx";
import EventSeatMap from "./gyp-pages/seat-maps/EventSeatMap.tsx";
import { SaleChannelResponseDto, TicketShopSaleChannelConfigDto } from "./models/generated/sale-channel-service-models";
import { SaleChannelService } from "./services/SaleChannel/SaleChannelService.ts";
import { extractOrgSlug } from "./utils/tenant.ts";

const GypApp: React.FC = () => {
    const [tenantOrgSlug, setTenantOrgSlug] = useState<string | null>(null);
    const [tenantOrganizationId, setTenantOrganizationId] = useState<string | null>(null);
    const [tenantSaleChannel, setTenantSaleChannel] = useState<SaleChannelResponseDto | null>(null);

    useEffect(() => {
        const initializeTenant = async () => {
            const slug = extractOrgSlug();
            setTenantOrgSlug(slug);

            if(!slug) {
                setTenantOrganizationId(null);
                setTenantSaleChannel(null);
                return;
            }

            try {
                const response = await SaleChannelService.getSaleChannelBySlug(slug);
                setTenantSaleChannel(response);
                setTenantOrganizationId(response?.organizationId ?? null);

                const config = response?.saleChannelConfig as TicketShopSaleChannelConfigDto | undefined;
                if(config?.primaryColor) {
                    document.documentElement.style.setProperty("--gyp-primary-color", config.primaryColor);
                }
                if(config?.secondaryColor) {
                    document.documentElement.style.setProperty("--gyp-secondary-color", config.secondaryColor);
                }
                if(config?.displayName) {
                    document.title = config.displayName;
                }
                if(config?.faviconUrl) {
                    const favicon = document.querySelector<HTMLLinkElement>("link[rel='icon']");
                    if(favicon) {
                        favicon.href = config.faviconUrl;
                    }
                }
            } catch (error) {
                console.error("Failed to load tenant sale-channel config", error);
            }
        };

        void initializeTenant();
    }, []);

    const routes = useRoutes([
        {
            path: "/",
            element: <GypRootLayout/>,
            children: [
                {
                    index: true,
                    element: <LandingPage/>,
                },
                {
                    path: "login",
                    element: <LoginPage/>,
                },
                {
                    path: "register",
                    element: <RegisterPage/>,
                },
                {
                    path: "organization/register",
                    element: <OrganizationRegisterPage/>,
                },
                {
                    path: "oauth2/callback",
                    element: <OAuth2CallbackPage/>,
                },
                {
                    path: "events/:id",
                    element: <EventDetailPage/>,
                },
                {
                    path: "events/:id/orders",
                    element: <EventOrderPage/>,
                },
                {
                    path: "payment/success",
                    element: <PaymentSuccessPage/>
                },
                {
                    path: "payment/failure",
                    element: <PaymentFailurePage/>,
                },
                {
                    path: "waiting-room",
                    element: <WaitingRoomPage/>,
                },
                {
                    path: "events/:id/waiting-room",
                    element: <WaitingRoomPage/>,
                },
            ]
        },
        {
            path: "events/:id/choose-seats",
            element: <EventSeatMap/>,
        },
    ]);

    return (
            <GypPageContext.Provider value={{ tenantOrgSlug, tenantOrganizationId, tenantSaleChannel }}>
                {routes}
            </GypPageContext.Provider>
    );
}

export default GypApp;