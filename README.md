# 🎓 Personalized Learning Experience App – Enhanced Edition

![Android](https://img.shields.io/badge/Platform-Android-blue.svg)
![Java](https://img.shields.io/badge/Language-Java-red.svg)
![SQLite](https://img.shields.io/badge/Database-SQLite-yellow.svg)
![LLM Powered](https://img.shields.io/badge/AI-LLM%20Integration-purple.svg)

<p align="center">
  <img src="app/src/main/ic_launcher.png" width="128" alt="App Logo" />
</p>

> A dynamic AI-integrated Android application that empowers learners with **tailored quizzes**, **profile statistics**, **learning history**, and **upgradable plans**—enhanced for SIT708 Task 10.1D.

---

## 📌 Table of Contents

* [📚 About](#-about)
* [✨ Features](#-features)
* [🛠️ Tech Stack](#-tech-stack)
* [🏗️ Architecture](#-architecture-overview)
* [📱 Screenshots](#-screenshots)
* [🚀 Getting Started](#-getting-started)
* [📋 Usage Flow](#-usage-flow)
* [🧠 LLM Role](#-llm-role)
* [🙌 Contributions](#-contributions)
* [📄 License](#-license)

---

## 📚 About

This version of the **Personalized Learning Experience App** extends the Task 6.1D project by introducing:

* 🔄 **History Screen**: Review past questions and answers  
* 📤 **Profile Sharing**: Generate and share QR with public stats  
* 💳 **Upgrade Plans**: Simulated in-app purchases for advanced plans  
* 💡 **LLM-powered quizzes**: Dynamically generated via `Gemma 3B`

---

## ✨ Features

| Category       | Feature Description                              |
| -------------- | ------------------------------------------------ |
| 👤 Profile     | View stats, update avatar, generate shareable QR |
| 📚 History     | See previous quiz attempts with full breakdown   |
| 🎓 Quizzes     | Adaptive questions from LLM based on interests   |
| 🔍 Feedback    | Detailed results with correctness highlights     |
| 💼 Plans       | Choose from Starter, Intermediate, Advanced      |
| 📤 Sharing     | Share public profile as QR (Lottie-based dialog) |
| 🔐 Secure Auth | Signup/Login with local validation               |
| 📱 UI Design   | Flexbox layout, Material Components, Lottie      |

---

## 🛠️ Tech Stack

| Layer           | Technology                       |
| --------------- | -------------------------------- |
| Language        | Java (AndroidX)                  |
| Architecture    | MVVM-ish modular structure       |
| Database        | SQLite                           |
| UI Components   | Material, Flexbox, RecyclerView  |
| QR & Animations | ZXing, Lottie                    |
| LLM Backend     | Flask + HuggingFace Transformers |
| Model Used      | `google/gemma-3-1b-it`           |

---

## 🏗️ Architecture Overview

```txt
MainActivity ──▶ RegisterActivity ──▶ InterestsActivity
                                   └─▶ DashboardActivity
                                          ├─▶ TaskActivity ──▶ ResultActivity
                                          ├─▶ ProfileActivity ──▶ Share QR
                                          ├─▶ HistoryActivity
                                          └─▶ UpgradeActivity
                                   
LLM API (Flask) ──▶ GET /getQuiz?topic=XYZ
````

---

## 📱 Screenshots

<details>
<summary>✨ Tap to Expand</summary>

| Login/Register                  | Interest Selection                  | Dashboard                      |
| ------------------------------- | ----------------------------------- | ------------------------------ |
| ![](docs/screenshots/login.png) | ![](docs/screenshots/interests.png) | ![](docs/screenshots/home.png) |

| Quiz Page                      | Result                            | History                           |
| ------------------------------ | --------------------------------- | --------------------------------- |
| ![](docs/screenshots/task.png) | ![](docs/screenshots/results.png) | ![](docs/screenshots/history.png) |

| Profile                           | Upgrade                           | Share                           |
| --------------------------------- | --------------------------------- | ------------------------------- |
| ![](docs/screenshots/profile.png) | ![](docs/screenshots/upgrade.png) | ![](docs/screenshots/share.png) |

</details>

---

## 🚀 Getting Started

```bash
git clone https://github.com/Tillu6/SIT708-task10.1.git
cd SIT708-task10.1
```

* 📱 Open project in Android Studio
* 🔄 Let Gradle sync and install dependencies
* ▶️ Run on Emulator or Device (Android API 30+)

---

## 📋 Usage Flow

1. **Register & Login**
2. **Select interests** (up to 10 topics)
3. **View dashboard** tailored to interests
4. **Start a quiz** → Answer questions
5. **Submit and view results**
6. **Visit Profile** → Share or upgrade
7. **Review History** of past quizzes anytime

---

## 🧠 LLM Role

* Topic-based prompt sent to `/getQuiz` (Flask backend)
* `Gemma 3B IT` generates 3 questions with options and answers
* Server parses model output and sends structured JSON to app

Example:

```json
{
  "quiz": [
    {
      "question": "What is polymorphism in OOP?",
      "options": ["Inheritance", "Encapsulation", "Overriding", "Abstraction"],
      "correct_answer": "Overriding"
    }
  ]
}
```

---

## 🙌 Contributions

You're welcome to fork and enhance:

```bash
git checkout -b feature/YourFeature
```

Contribute ideas like:

* Real Google Pay integration
* Firebase authentication
* MongoDB backend (cloud sync)

---

## 📄 License

MIT © 2024 Tillu6 | Deakin University – SIT708

---
