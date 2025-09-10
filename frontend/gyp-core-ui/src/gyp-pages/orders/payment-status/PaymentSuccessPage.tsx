import React from "react";

const PaymentSuccessPage: React.FC = () => {
    const {orderId, message} = Object.fromEntries(new URLSearchParams(window.location.search));

    return (
            <div className="flex flex-col items-center justify-center bg-green-100 !p-4 flex-grow">
                <div className="bg-white shadow-md rounded-lg !p-8 max-w-md w-full text-center">
                    <h1 className="text-3xl font-bold text-green-600 mb-4">Payment {message} with order {orderId}</h1>
                    <p className="text-gray-700 mb-6">
                        Thank you for your purchase! Your payment has been processed successfully. You will receive a
                        confirmation email shortly with your order details.
                    </p>
                    <button
                            onClick={() => window.location.href = '/gyp/'}
                            className="bg-green-600 !text-white !p-2 rounded hover:bg-green-700 transition cursor-pointer"
                    >
                        Back to Home
                    </button>
                </div>
            </div>
    );
};

export default PaymentSuccessPage;