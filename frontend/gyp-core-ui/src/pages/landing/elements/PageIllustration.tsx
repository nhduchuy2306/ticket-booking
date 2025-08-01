import React from "react";
import Stripes from "../../../assets/stripes.svg";

const PageIllustration: React.FC = () => {
    return (
            <div
                    className="pointer-events-none absolute left-1/2 top-0 -z-10 -translate-x-1/2 transform"
                    aria-hidden="true"
            >
                <img
                        className="max-w-none"
                        src={Stripes}
                        width={768}
                        alt="Stripes"
                />
            </div>
    );
}

export default PageIllustration;
