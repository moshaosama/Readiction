import { AlignJustify } from "lucide-react";
import Image from "next/image";
import Link from "next/link";
import React from "react";

const Navbar = () => {
  return (
    <>
      <div className="Container flex justify-between items-center px-5 rounded-full bg-white ">
        <div>
          <Image
            src={"/Readiction_Website_Logo.png"}
            alt="Banner.png"
            width={80}
            height={80}
            className="max-sm:w-14"
          />
        </div>

        <div className="flex gap-14 max-sm:hidden font-bold text-[#727272]">
          <Link href={"/"}>Home</Link>
          <Link href={"/"}>Our services</Link>
          <Link href={"/"}>Contact us</Link>
        </div>

        <div className="flex gap-5 max-sm:hidden">
          <button className="border-2 border-[#f66623] cursor-pointer hover:bg-[#f66623] hover:text-white transition-all duration-300 w-24 py-2 rounded-full text-[#f66623] font-bold">
            Login
          </button>

          <button className="border-2  bg-[#f66623] cursor-pointer hover:bg-[#f66623d3] transition-all duration-300 w-24 py-2 rounded-full text-white font-bold">
            Signup
          </button>
        </div>

        <div className="sm:hidden">
          <AlignJustify />
        </div>
      </div>
    </>
  );
};

export default Navbar;
