import Image from "next/image";
import Link from "next/link";
import React from "react";

const Footer = () => {
  return (
    <>
      <div className="bg-[#f66623ad] w-full flex flex-col py-10 justify-center gap-3 items-center text-3xl font-bold text-white">
        <h1>Ready to start?</h1>
        <p>Join thousands of readers today.</p>

        <div>
          <button className="text-sm bg-white  text-black p-3 rounded-full">
            Create your free account now
          </button>
        </div>
      </div>

      <div
        className="Container flex justify-between items-center"
        style={{ marginTop: "0px", marginBottom: "0px" }}
      >
        <Image
          src={"/Readiction_Website_Logo.png"}
          alt="banner.png"
          width={70}
          height={70}
        />

        <div className="text-[#727272]">
          <p>2025 reads. All rights reserved to the Yqraa platform.</p>
        </div>
      </div>
    </>
  );
};

export default Footer;
