import React from "react";
import Main from "./Main.tsx";
import PageIllustration from "./PageIllustration.tsx";

const LandingPage: React.FC = () => {
    return (
            <div className="flex flex-col items-center">
                <PageIllustration/>
                <Main/>
            </div>
    );
}

export default LandingPage;