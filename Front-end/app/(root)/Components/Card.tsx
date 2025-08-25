import React from "react";
import { CardDataProps } from "../page";

const Card = ({ Title, Description, icon }: CardDataProps) => {
  return (
    <>
      <div className="flex flex-col gap-3 my-20 max-sm:my-0 max-sm:mt-20 items-center justify-center border-2 border-[#d6d6d6] shadow-2xl py-8 rounded-xl">
        <i className="bg-[#f66623ad] w-10 h-10 flex justify-center items-center rounded-2xl text-[#4b1800]">
          {icon}
        </i>

        <div className="flex flex-col gap-4 justify-center items-center">
          <h1 className="text-xl font-bold">{Title}</h1>
          <p className="text-center">{Description}</p>
        </div>
      </div>
    </>
  );
};

export default Card;
