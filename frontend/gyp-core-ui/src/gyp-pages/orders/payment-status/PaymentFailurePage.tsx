import React from "react";

const PaymentFailurePage: React.FC = () => {
    return (
            <div className="flex flex-col items-center justify-center bg-red-100 !p-4 flex-grow">
                <div className="bg-white shadow-md rounded-lg !p-8 max-w-md w-full text-center">
                    <h1 className="text-3xl font-bold text-red-600 mb-4">Payment Failed</h1>
                    <p className="text-gray-700 mb-6">
                        Unfortunately, your payment could not be processed at this time. Please try again or contact
                        support if the issue persists.
                    </p>
                    <button
                            onClick={() => window.location.reload()}
                            className="bg-red-600 !text-white !p-2 rounded hover:bg-red-700 transition cursor-pointer"
                    >
                        Retry Payment
                    </button>
                </div>
            </div>
    );
}

export default PaymentFailurePage;