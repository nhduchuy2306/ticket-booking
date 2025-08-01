import React from "react";
import Footer from "./elements/Footer.tsx";
import Header from "./elements/Header.tsx";
import Main from "./elements/Main.tsx";
import PageIllustration from "./elements/PageIllustration.tsx";

const LandingPage: React.FC = () => {
    return (
            <div className="flex flex-col items-center">
                <Header/>
                <PageIllustration/>
                <Main/>
                <Footer/>
            </div>
    );
}

export default LandingPage;