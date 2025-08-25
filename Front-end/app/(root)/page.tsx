import React, { ReactNode } from "react";
import Hero from "./Components/Hero";
import Text from "./Components/Text";
import Card from "./Components/Card";
import { Book, Languages, MessageCircleCodeIcon } from "lucide-react";

export interface CardDataProps {
  icon: ReactNode;
  Title: string;
  Description: string;
}

const CardData: CardDataProps[] = [
  {
    icon: <Book />,
    Description:
      "Easily add books you're reading or have read. Build your digital library that reflects your learning journey.",
    Title: "Your own library",
  },

  {
    icon: <MessageCircleCodeIcon />,
    Description:
      "Don't let any inspiring thought slip away. Write down the quotes that touch your soul and add notes to them.",
    Title: "Quote Hunter",
  },

  {
    icon: <Languages />,
    Description:
      "Come across a new word? Add it and its meaning to your dictionary to expand your vocabulary.",
    Title: "Your personal dictionary",
  },
];

const page = () => {
  return (
    <div className="Container">
      <Hero />
      <Text />
      <div className="flex max-sm:flex-col justify-center gap-10 max-sm:gap-0">
        {CardData.map((cardata, index: number) => {
          return (
            <span key={index}>
              <Card
                Description={cardata.Description}
                Title={cardata.Title}
                icon={cardata.icon}
              />
            </span>
          );
        })}
      </div>
    </div>
  );
};

export default page;
