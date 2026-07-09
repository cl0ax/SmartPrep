export const INTAKE_QUESTIONS = [
  {
    id: "experience_level",
    type: "single_select",
    prompt: "What best describes your Java programming experience?",
    options: [
      "Beginner - Limited Java experience. Learning fundamental programming concepts and simple problem solving.",
      "Intermediate - Comfortable with core Java concepts and basic data structures. Can solve standard problems with minimal guidance.",
      "Experienced - Confident writing Java programs and solving intermediate algorithmic problems independently."
    ]
  },

  {
    id: "topics.arrays_strings",
    type: "rating_1_5",
    prompt: "Rate your comfort level with Arrays & Strings",
    categoryId: 1
  },

  {
    id: "topics.two_pointers",
    type: "rating_1_5",
    prompt: "Rate your comfort level with Two Pointers",
    categoryId: 2
  },

  {
    id: "topics.hash_maps_sets",
    type: "rating_1_5",
    prompt: "Rate your comfort level with Hash Maps / Sets",
    categoryId: 3
  }
];