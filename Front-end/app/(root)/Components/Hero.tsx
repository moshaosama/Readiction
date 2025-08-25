import React from "react";

const Hero = () => {
  return (
    <>
      <div className="w-full flex flex-col items-center gap-5 justify-center mt-20">
        <h1 className="text-5xl font-bold w-[65pc] text-center max-sm:text-xl max-sm:w-full">
          Turn your reading into lasting wisdom. Collect{" "}
          <span className="text-[#f66623]">your quotes</span> in one place!
        </h1>

        <p className="text-[gray] text-lg">
          The “Iqtibas” platform helps you write down your inspiring quotes and
          distinctive words to organize your knowledge.
        </p>

        <div className="flex gap-5 my-14">
          <button className="border-2 border-[#f66623] cursor-pointer  transition-all duration-300 py-2 px-3 rounded-full text-[#f66623] font-bold">
            Start your knowledge journey
          </button>

          <button className="border-2  bg-[#f66623] cursor-pointer transition-all duration-300 border-none  py-2 px-3 rounded-full text-white font-bold">
            See how it works
          </button>
        </div>
      </div>
    </>
  );
};

export default Hero;
