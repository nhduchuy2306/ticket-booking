import { Alert, Spin, Typography } from "antd";
import React, { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import AuthLayout from "../../components/auth/AuthLayout.tsx";
import { CustomerAuthService } from "../../services/CustomerAuth/CustomerAuthService.ts";
import { CustomerAuthStorage } from "../../services/CustomerAuth/CustomerAuthStorage.ts";
import "./auth.scss";
import { useGypPageContext } from "../GypPageContext.tsx";

const {Text} = Typography;

const OAuth2CallbackPage: React.FC = () => {
    const [searchParams] = useSearchParams();
    const [message, setMessage] = useState<string>("Completing Google sign-in...");
    const [isError, setIsError] = useState<boolean>(false);
    const navigate = useNavigate();
    const {setCustomerResponseDto} = useGypPageContext();
    

    useEffect(() => {
        const accessToken = searchParams.get("accessToken");
        const refreshToken = searchParams.get("refreshToken");
        const expiresIn = searchParams.get("expiresIn");
        const error = searchParams.get("error") || searchParams.get("oauth2Error");

        if (error) {
            setIsError(true);
            setMessage(error);
            window.setTimeout(() => {
                navigate(`/login?oauth2Error=${encodeURIComponent(error)}`);
            }, 2500);
            return;
        }

        if (!accessToken || !refreshToken) {
            setIsError(true);
            setMessage("Google login did not return the expected tokens.");
            window.setTimeout(() => {
                navigate("/login?oauth2Error=missing_tokens");
            }, 2500);
            return;
        }

        CustomerAuthStorage.setTokens(accessToken, refreshToken, expiresIn ? Number(expiresIn) : 900);

        (async () => {
            try {
                const me = await CustomerAuthService.getMe();
                setCustomerResponseDto?.(me);
                CustomerAuthStorage.setCustomerName(me.name);
            } catch (meError) {
                console.error("Failed to load current customer after OAuth2 sign-in", meError);
            }
        })();

        const redirectPath = CustomerAuthStorage.consumeRedirectPath() || "/gyp/";
        window.history.replaceState({}, document.title, window.location.pathname);
        setMessage("Signed in successfully. Redirecting you back to your session...");
        window.setTimeout(() => {
            navigate(redirectPath, {replace: true});
        }, 1200);
    }, [navigate, searchParams, setCustomerResponseDto]);

    return (
            <AuthLayout
                    title="Google sign-in"
                    description="Your customer session is being restored with the tokens issued by the backend."
            >
                {!isError && (
                        <div style={{display: "grid", placeItems: "center", minHeight: 180}}>
                            <Spin size="large"/>
                        </div>
                )}
                <Alert
                        className="customer-auth-alert"
                        type={isError ? "error" : "success"}
                        message={isError ? "OAuth2 sign-in failed" : "Google login complete"}
                        description={message}
                        showIcon
                />
                <div className="customer-auth-meta" style={{marginTop: 18}}>
                    <Text style={{color: "rgba(226, 232, 240, 0.82)"}}>Tokens are stored separately from internal user
                        sessions.</Text>
                </div>
            </AuthLayout>
    );
};

export default OAuth2CallbackPage;