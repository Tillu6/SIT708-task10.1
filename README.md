# 🚀 Personalized Learning Experience App
![Platform: Android](https://img.shields.io/badge/Platform-Android-blue.svg)
![Min SDK: 21](https://img.shields.io/badge/Min%20SDK-21-brightgreen.svg)
![Compile SDK: 35](https://img.shields.io/badge/Compile%20SDK-35-blue.svg)
![Language: Java](https://img.shields.io/badge/Language-Java-red.svg)
<p align="center">
  <img src="app/src/main/ic_launcher.png" alt="🔥" width="164" />
</p>


> A futuristic, AI-inspired Android application that delivers adaptive, personalized learning experiences—tailored quizzes, intelligent tutoring flows, and dynamic assessments based on students’ interests and past interactions.

---

## 📖 Table of Contents

- [✨ About](#-about)  
- [🚀 Features](#-features)  
- [🛠️ Tech Stack](#️-tech-stack)  
- [📐 Architecture](#-architecture)  
- [🎨 UI / UX Highlights](#-ui--ux-highlights)  
- [⚙️ Getting Started](#️-getting-started)  
- [📝 Usage](#-usage)  
- [🤝 Contributing](#-contributing)   

---

## ✨ About

The **Personalized Learning Experience App** is a prototype Android application built for SIT706 Task 6.1. It leverages modern UI components, Lottie animations, and a simple local database to simulate a fully adaptive learning platform. Students can:

1. **Sign up** and pick their areas of interest  
2. **Log in** and view a custom task dashboard  
3. **Complete AI-generated quizzes** with multiple question types  
4. **Review results** enriched with static or AI-driven feedback  

> “Empower students with learning that adapts to them — not the other way around.”  

---

## 🚀 Features

- 🔒 **Secure Authentication**  
  - Sign-up / Login flow with field validation  
  - Local SQLite storage via `DBHelper`

- 🎯 **Interest Selection**  
  - Material Chips to pick up to 10 topics  
  - Persists selection for future personalization  

- 🧩 **Adaptive Quiz Engine**  
  - Multi-screen quiz (radio-button questions, toggles, …)  
  - “Next” arrow navigation between question cards  
  - Dynamic question set (3 questions by default)  

- 📈 **Results & Feedback**  
  - Displays student answers  
  - Placeholder for AI-driven model responses  

- 🎨 **Modern UI**  
  - Gradient backgrounds & neumorphic cards  
  - Lottie “robot brain” animations  
  - Material Components & CardViews  

---

## 🛠️ Tech Stack

| Layer            | Technology                        |
| ---------------- | --------------------------------- |
| 📱 Platform      | Android (SDK 35+)                 |
| 🛠️ Language      | Java (AndroidX)                   |
| 📊 Database      | SQLite (`SQLiteOpenHelper`)       |
| 🎨 UI Toolkit    | Material Components, Lottie       |
| ⚙️ Build System  | Gradle Kotlin DSL                 |

---

## 📐 Architecture

```txt
┌────────────┐      ┌───────────────┐      ┌──────────────┐
│ MainActivity│─▶ SignUpActivity ─▶ InterestsActivity ─▶ HomeActivity ─▶ TaskActivity ─▶ ResultsActivity
│  (Splash)  │      │   (Form)      │      │  (Chips)     │      │ (Dashboard) │      │ (Quiz) │      │ (Feedback) │
└────────────┘      └───────────────┘      └──────────────┘      └──────────────┘      └──────────┘      └───────────┘
        │                                ▲                                         ▲
        └────────── DBHelper ────────────┘─────────────────────────────────────────┘
```

---

## 🎨 UI / UX Highlights

| Screen                | Description                              |
| --------------------- | ---------------------------------------- |
| **Sign Up & Login**   | Lottie banner, form validation           |
| **Interests**         | Select up to 10 topics via Chips         |
| **Home Dashboard**    | “Hello, [User]”, task summary card       |
| **Adaptive Quiz**     | CardView questions, radio & toggle inputs|
| **Results**           | Clear summary + AI-placeholder feedback  |

<details>
<summary>Click to expand sample screenshots</summary>

![Login / Sign Up](docs/screenshots/login.png)  
![Interests](docs/screenshots/interests.png)  
![Dashboard](docs/screenshots/home.png)  
![Quiz](docs/screenshots/task.png)  
![Results](docs/screenshots/results.png)  

</details>

---

## ⚙️ Getting Started

1. **Clone this repo**
   ```bash
   git clone https://github.com/Tillu6/SIT706-6.1D_Personalized-Learning-Experience-App.git
   cd SIT706-6.1D_Personalized-Learning-Experience-App
   ```

2. **Open in Android Studio**
   - File → Open → select project root  
   - Let Gradle sync & download dependencies

3. **Run on Device / Emulator**
   - API level 30+ recommended  
   - Hit ▶️ **Run** 

---

## 📝 Usage

1. **Sign up** with unique username & password  
2. **Select interests** to personalize your experience  
3. **Log in** and view your dashboard  
4. **Tap the arrow** on the task card to start a quiz  
5. **Answer questions**, tap **Next** to navigate  
6. **Submit** and review your results  

> Feel free to swap the dummy data with real API calls or integrate an LLM backend!

---

## 🤝 Contributing

1. Fork the repository  
2. Create your feature branch (`git checkout -b feature/XYZ`)  
3. Commit your changes (`git commit -m 'Add XYZ feature'`)  
4. Push to the branch (`git push origin feature/XYZ`)  
5. Open a Pull Request  

Please follow the existing code style and commit conventions.

---

> Crafted with ❤️ and ☕ by the Personalized Learning Team  
