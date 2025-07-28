import React, { useEffect, useState } from "react";
import { CgProfile } from "react-icons/cg";
import { useNavigate } from "react-router-dom";

const ProfilePage: React.FC = () => {
    const [username, setUsername] = useState<string | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        const storedUsername = localStorage.getItem('username');
        if (storedUsername) {
            setUsername(storedUsername);
        } else {
            console.warn("Username not found in localStorage.");
        }
    }, []);

    const handleClick = () => {
        if (username) {
            navigate('/profile-detail');
        } else {
            console.warn("Username is not set, cannot navigate to profile detail.");
        }
    }

    return (
            <div
                    className="flex flex-row gap-2 items-center justify-center bg-gray-400 p-4! cursor-pointer"
                    onClick={handleClick}
            >
                <div><CgProfile className="size-6"/></div>
                <div>{username}</div>
            </div>
    );
}

export default ProfilePage;