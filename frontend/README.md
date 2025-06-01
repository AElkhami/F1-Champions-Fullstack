# 🏎️ F1 World Champions Android App

This Android app showcases current and historical Formula 1 world champions. Built using **Kotlin**, **Jetpack Compose**, and **MVI Clean Architecture**, it provides a smooth, scalable, and testable mobile experience powered by a robust CI/CD pipeline.

---

## 📱 Features

- View list of F1 World Champions by season
- Browse race wins per season
- Beautiful Jetpack Compose UI
- Lightweight single-module setup with scalable package structure
- CI/CD pipeline with lint, tests, and release automation
- Secure code scanning via CodeQL

---

## 🔧 Architecture Overview

### ✅ High-Level Components

- **Jetpack Compose** for declarative UI
- **MVI Clean Architecture** for predictable state management
- **Kotlin Coroutines** for background work
- **Dependency Injection** with Hilt
- **Repository pattern** for abstraction
- **Well-structured package hierarchy** to mimic multi-module scalability

### ⚖️ Trade-Offs Considered

| Area                          | Decision |
|-------------------------------|----------|
| **Compose vs XML**            | Compose for modern, declarative UIs |
| **Single vs Multi-Module**    | Single module with modular package structure for easier future refactoring |
| **MVVM vs MVI**               | MVI for clear state flow and better testability |
| **Live Data vs Compose State**| `mutableStateOf` used for UI state management (scalable to StateFlow if needed) |

---

## 🚀 Run Locally

1. Open the project in **Android Studio**
2. Select the `:app` module
3. Connect a device or use an emulator
4. Click **Run**

---

## 🧪 Run Unit Tests

```bash
./gradlew testDebugUnitTest
```

This command runs all unit tests associated with the debug variant.

---

## 🔁 CI/CD Pipeline (GitHub Actions)

The project includes a full CI pipeline triggered on:
- Push to `master`
- Pull requests to `master`

### CI Stages:
- ✅ Setup (Gradle, JDK, Android SDK)
- ✅ Lint
- ✅ Unit Tests
- ✅ Build APK
- ✅ CodeQL Security Scan
- ✅ GitHub Release Upload (with APK asset)

To trigger manually (if enabled):

---

## 📦 APK Release

Built APKs are automatically uploaded to the **GitHub Releases** tab on each successful build of the `master` branch. You can download the latest APK from:

[Releases](https://github.com/AElkhami/F1-Champions-Android/releases)

---

## 🔌 API Contract

### `GET /f1/champions`

Returns all available world champions.

#### Sample Response:
```json
[
  {
    "season": "2023",
    "driverName": "Max Verstappen",
    "constructor": "Red Bull Racing"
  }
]
```

---

### `GET /f1/champions/{season}`

Returns race wins for the champion of the given season.

#### Sample Response:
```json
[
  {
    "round": "7",
    "raceName": "Monaco Grand Prix",
    "date": "2023-05-28",
    "winnerName": "Max Verstappen",
    "constructor": "Red Bull Racing"
  }
]
```

---

## 🧩 Tech Stack

- Kotlin
- Jetpack Compose
- Hilt (Dependency Injection)
- GitHub Actions (CI/CD)
- CodeQL (Security Scanning)

---

## 📁 Project Structure

```
app/
└── src/
    └── main/
        └── java/
            ├── feature/
            │   ├── data/                # Manages data retrieval, transformation, and DI setup
            │   │   ├── di/              # Hilt modules and component providers for dependency injection
            │   │   ├── mapper/          # Maps remote/local DTOs to domain models and vice versa
            │   │   ├── model/           # DTOs or data-layer-specific models for API or storage
            │   │   ├── remote/          # Retrofit interfaces and networking logic
            │   │   └── repository/      # Concrete data source implementations coordinating remote/local sources
            │   ├── domain/              # Holds core business logic and abstract contracts
            │   │   ├── mapper/          # Domain-specific model converters or value transformations
            │   │   └── repository/      # Interfaces representing data operations, used by the presentation layer
            │   └── presentation/        # UI-facing layer: state management, view logic, and Compose screens
            │       ├── screen/          # Compose screen composables for rendering the UI
            │       ├── state/           # Data classes representing UI state and events
            │       └── viewmodel/       # ViewModels orchestrating UI logic, interact with domain layer
            ├── core/                    # Reusable components, utilities, and extensions shared app-wide
            └── navigation/              # Manages app navigation destinations and routes with Compose Navigation

```

---

## 🧠 About the Author

This project was built by a **Senior Android Engineer** with 8+ years of experience in crafting performant, maintainable Android applications using modern best practices.

---

## 📄 License

MIT — use freely, contribute back if you can 🚀
