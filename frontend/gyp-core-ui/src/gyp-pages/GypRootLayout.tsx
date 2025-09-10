import React from "react";
import { Outlet } from "react-router-dom";
import Header from "./landing/Header.tsx";

const GypRootLayout: React.FC = () => {
    return (
            <div className="flex flex-col min-h-screen">
                <Header/>
                <main className="flex-grow flex flex-col">
                    <Outlet/>
                </main>
                <footer className="bg-[#1d1d1d]">
                    <div className="flex justify-center items-center">
                        <span className="!p-4 text-white">Copyright © 2024 GYP by GYPSY</span>
                    </div>
                </footer>
            </div>
    );
};

export default GypRootLayout;