import { Button, Input, Space } from "antd";
import React from "react";
import { BiSearch } from "react-icons/bi";
import { IoTicketOutline } from "react-icons/io5";
import { Link, useNavigate } from "react-router-dom";
import Logo from "./Logo.tsx";

const Header: React.FC = () => {
    const navigate = useNavigate();

    const handleMyTicketClick = () => {
        console.log("My Ticket clicked");
    }

    const handleCreateEventClick = () => {
        navigate("/");
    }

    return (
            <header className="mx-auto px-4 w-full shadow-sm">
                <div className="flex h-[76px] items-center justify-center bg-[#2dc275] !pl-5 !pr-5">
                    <div className="flex items-center">
                        <Logo/>
                    </div>

                    <div className="flex-1 flex justify-center">
                        <Space.Compact size="large" className="bg-white rounded-md w-[400px]">
                            <Input
                                    addonBefore={<BiSearch/>}
                                    placeholder="Search..."
                                    className="bg-white rounded-md flex-1"
                            />
                        </Space.Compact>
                    </div>

                    <div className="flex items-center gap-4">
                        <div className="flex items-center text-white cursor-pointer hover:opacity-80"
                             onClick={handleMyTicketClick}>
                            <IoTicketOutline className="text-xl mr-1"/>
                            <span>My Ticket</span>
                        </div>

                        <Button
                                type="default"
                                className="bg-white rounded-full font-medium px-5"
                                onClick={handleCreateEventClick}
                        >
                            Create Event
                        </Button>

                        <div className="flex items-center gap-2">
                            <Button type="primary">
                                <Link to="/login">Login</Link>
                            </Button>
                            <Button>
                                <Link to="/sign-up">Register</Link>
                            </Button>
                        </div>
                    </div>
                </div>
            </header>
    );
};

export default Header;