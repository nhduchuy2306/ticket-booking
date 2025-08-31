import { Switch, Tooltip } from "antd";
import React, { useEffect, useState } from "react";
import { CgProfile } from "react-icons/cg";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { IamService } from "../../services/Iam/IamService.ts";
import { toggleBoxOfficeMode } from "../../states/reducers/boxOfficeSlice.ts";
import { AppDispatch } from "../../states/store.ts";

export interface ProfilePageProps {
    collapsed?: boolean;
}

const ProfilePage: React.FC<ProfilePageProps> = ({collapsed = false}) => {
    const [name, setName] = useState<string | null>(null);
    const [isBoxOfficeMode, setIsBoxOfficeMode] = useState(JSON.parse(localStorage.getItem('boxOfficeMode') || 'false'));
    const navigate = useNavigate();
    const dispatch = useDispatch<AppDispatch>();

    useEffect(() => {
        const storedName = IamService.getName();
        if (storedName) {
            setName(storedName);
        } else {
            console.warn("Username not found in localStorage.");
        }
    }, []);

    const handleProfileClick = () => {
        if (name) {
            navigate('/profile-detail');
        } else {
            console.warn("Username is not set, cannot navigate to profile detail.");
        }
    }

    const onSwitchChange = (checked: boolean) => {
        setIsBoxOfficeMode(checked);
        dispatch(toggleBoxOfficeMode(checked));
    };

    if (collapsed) {
        return (
                <div className="flex flex-col items-center gap-2 bg-white rounded-lg px-2 py-3 border border-gray-200 hover:shadow-lg transition-all duration-300 cursor-pointer group !m-1 !p-2">
                    {/* Profile Icon Only */}
                    <div onClick={handleProfileClick}>
                        <Tooltip title={name} placement="right">
                            <div className="w-10 h-10 bg-gradient-to-br from-pink-500 to-orange-400 rounded-full flex items-center justify-center shadow-sm group-hover:scale-105 transition-transform duration-300">
                                <CgProfile className="w-6 h-6 text-white"/>
                            </div>
                        </Tooltip>
                    </div>

                    {/* Box Office Switch - Vertical */}
                    <div className="flex flex-col items-center gap-1 mt-2">
                        <Tooltip
                                title={`Box Office mode: ${isBoxOfficeMode ? 'ON' : 'OFF'}`}
                                placement="right"
                        >
                            <Switch
                                    size="small"
                                    checked={isBoxOfficeMode}
                                    onChange={onSwitchChange}
                                    style={{
                                        backgroundColor: isBoxOfficeMode ? '#1890ff' : undefined,
                                    }}
                            />
                        </Tooltip>
                    </div>
                </div>
        );
    }

    // Expanded view - full layout
    return (
            <div className="flex items-center gap-4 bg-white rounded-lg px-4 py-3 border border-gray-200 hover:shadow-lg transition-all duration-300 cursor-pointer group !m-1 !p-2">
                {/* Profile Section */}
                <div className="flex items-center gap-3 flex-1" onClick={handleProfileClick}>
                    <div className="flex items-center gap-1">
                        <div className="!mr-2">
                            <div className="w-10 h-10 bg-gradient-to-br from-pink-500 to-orange-400 rounded-full flex items-center justify-center shadow-sm group-hover:scale-105 transition-transform duration-300">
                                <CgProfile className="w-6 h-6 text-white"/>
                            </div>
                        </div>
                        <span className="font-medium text-gray-800 group-hover:text-gray-900">{name}</span>
                    </div>

                </div>

                {/* Divider */}
                <div className="w-px h-6 bg-gray-200"></div>

                {/* Box Office Switch Section */}
                <div className="flex items-center gap-2">
                    <span className="text-sm font-medium text-gray-700">Box Office</span>
                    <Tooltip
                            title={isBoxOfficeMode ? "Disable Box Office mode" : "Enable Box Office mode"}
                            placement="right"
                    >
                        <Switch
                                size="small"
                                checked={isBoxOfficeMode}
                                onChange={onSwitchChange}
                                style={{
                                    backgroundColor: isBoxOfficeMode ? '#1890ff' : undefined,
                                }}
                        />
                    </Tooltip>
                </div>
            </div>
    );
}

export default ProfilePage;