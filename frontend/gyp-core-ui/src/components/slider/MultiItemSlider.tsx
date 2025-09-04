import React, { useState } from "react";
import { IoIosArrowBack, IoIosArrowForward } from "react-icons/io";

interface MultiItemSliderProps {
    itemsPerView?: number;
    items?: React.ReactNode[];
    onItemClick?: (index: number) => void;
}

const MultiItemSlider: React.FC<MultiItemSliderProps> = ({itemsPerView = 3, items, onItemClick}) => {
    const [currentIndex, setCurrentIndex] = useState(0);
    const maxIndex = Math.max(0, (items ? Math.round(items.length / itemsPerView) : 0) - 1);

    const nextSlide = () => {
        setCurrentIndex((prev) => Math.min(prev + 1, maxIndex));
    };

    const prevSlide = () => {
        setCurrentIndex((prev) => Math.max(prev - 1, 0));
    };

    return (
            <div className="w-full p-4 rounded-xl">
                <div className="relative rounded-xl !p-0">
                    <div className="overflow-hidden relative">
                        {/* Prev button */}
                        {currentIndex != 0 && <button
                            className="absolute top-1/2 -translate-y-1/2 left-0 rounded-r-xl bg-black opacity-30 hover:opacity-90 transition duration-300 ease-in-out z-10 cursor-pointer"
                            onClick={prevSlide}
                        >
                            <IoIosArrowBack className="size-8 text-white"/>
                        </button>}

                        {/* Slider container */}
                        <div
                                className="flex transition-transform duration-300 ease-in-out gap-4"
                                style={{
                                    transform: `translateX(-${currentIndex * (100 / itemsPerView)}%)`,
                                    width: `${items ? Math.round((items.length * 100) / itemsPerView) : 0}%`,
                                }}
                        >
                            {items?.map((item, index) => (
                                    <div
                                            key={index}
                                            className="bg-white rounded-xl cursor-pointer !p-4 border border-gray-200"
                                            style={{width: `${100 / itemsPerView}%`}}
                                            onClick={() => onItemClick && onItemClick(index)}
                                    >
                                        {item}
                                    </div>
                            ))}
                        </div>

                        {/* Next button */}
                        {currentIndex < maxIndex && <button
                            className="absolute top-1/2 -translate-y-1/2 right-0 rounded-l-xl bg-black opacity-30 hover:opacity-90 transition duration-300 ease-in-out z-10 cursor-pointer"
                            onClick={nextSlide}
                        >
                            <IoIosArrowForward className="size-8 text-white"/>
                        </button>}
                    </div>
                </div>
            </div>
    );
};

export default MultiItemSlider;
