import React from "react";
import { Outlet } from "react-router-dom";
import Footer from "./landing/Footer.tsx";
import Header from "./landing/Header.tsx";

const GypRootLayout: React.FC = () => {
    return (
            <div className="flex flex-col min-h-screen">
                <Header />
                <main className="flex-grow w-full">
                    <Outlet />
                </main>
                <Footer />
            </div>
    );
};

export default GypRootLayout;