import { Button } from "antd";
import React from "react";
import { Link } from "react-router-dom";
import Logo from "./Logo.tsx";

const Header: React.FC = () => {
    return (
            <header className="!mt-5 mx-auto px-4 w-full max-w-4xl">
                <div className="flex h-14 items-center justify-center gap-3 rounded-2xl bg-white/90 px-3 shadow-lg shadow-black/[0.03] backdrop-blur-xs before:pointer-events-none before:absolute before:inset-0 before:rounded-[inherit] before:border before:border-transparent before:[background:linear-gradient(var(--color-gray-100),var(--color-gray-200))_border-box] before:[mask-composite:exclude_!important] before:[mask:linear-gradient(white_0_0)_padding-box,_linear-gradient(white_0_0)]">
                    <div className="float-left flex items-center justify-center !ml-2">
                        <Logo/>
                    </div>
                    <div className="flex flex-1 items-center justify-end gap-3 !mr-2">
                        <Button type="primary">
                            <Link to="/login">Login</Link>
                        </Button>
                        <Button type="default">
                            <Link to="/sign-up">Register</Link>
                        </Button>
                    </div>
                </div>
            </header>
    );
}

export default Header;