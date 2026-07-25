# 🤖 JARVES - Futuristic AI Voice Assistant Android App

**JARVES** is a futuristic, dark-themed Android AI Voice Assistant application built with Kotlin & Jetpack Compose. It handles multilingual natural language voice commands (Hindi, English, Hinglish), compound multitasking sequences, Siri-style floating HUD animations, and full device control.

---

## 🌟 Key Features

- **🗣️ Multilingual Voice Recognition (Hindi + English):** Understands natural speech and compound sentences like *"भाई, मम्मी को कॉल लगा और उसके बाद कैमरा खोल देना"*.
- **✨ Siri-Style Floating HUD Pop-Up:** Animated glowing AI visualizer sphere (`SYSTEM_ALERT_WINDOW`) that pops up over any active app when you call *"Jarves"*.
- **📱 Multitasking & Device Controls:**
  - **Phone Calls:** Automatic contact resolution & dialing ("Call Mummy").
  - **Camera & Video:** Instant camera launcher, photo capture, video recording.
  - **Flashlight:** Voice-controlled torch ON/OFF.
  - **Battery Reader:** Real-time battery percentage and charging state spoken via Text-To-Speech (TTS).
  - **YouTube Playback:** Play songs or search videos ("YouTube पर Arijit Singh के गाने चलाओ").
  - **Alarms & Reminders:** Set exact alarms and countdown reminders.
  - **Scheduled SMS:** Send delayed text messages ("send SMS 'HII' to mummy after 30 minutes").
- **🔐 Auth System (Login & Sign Up):** Dark glassmorphic user profile & authentication using Firebase.
- **⚡ 100% Free Architecture:** Built using zero-cost tiers (Firebase, Android Native Speech & TTS, GitHub Actions CI/CD).
- **🔄 GitHub Automated Builds & In-App Updates:** Automatically compiles APKs on push via GitHub Actions.

---

## 🚀 Getting Started

### Requirements
- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17
- Android SDK 34 (Android 14+)

### Building the App
```bash
# Clone the repository
git clone https://github.com/your-username/JARVES.git

# Navigate to project folder
cd JARVES

# Build Debug APK
./gradlew assembleDebug
```

---

## 📄 License
This project is open-source and free to use.
