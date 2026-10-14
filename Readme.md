# Field Operations Mobile App (Offline-First)
An offline Field Operations app to manage tasks.

A field operations Android application built with Kotlin, Jetpack Compose, and Hilt. 
Designed for field technicians, the app handles task creation, real-time GPS location tagging, photo uploads, and resilient background synchronization when network connectivity fluctuates.

---

## Architecture & Tech Stack

The app follows Modern Android Development (MAD) standards and Clean MVVM Architecture with a Single Source of Truth (Room).

UI Layer: Jetpack Compose, Material 3, ViewModel, StateFlow.
Local Storage: Room SQLite Database.
Dependency Injection: Hilt.
Background Processing: WorkManager (Network-constrained background sync & retry logic).
Networking: Retrofit 2, OkHttp 4, Interceptors.
Security: EncryptedSharedPreferences (Token Storage), Network Security Configuration.
Hardware & Location: Google FusedLocationProviderClient, Activity Result Contracts (Camera).

---

##  Key Features

Offline-First Persistence: All local task edits, status updates, and creations save immediately to Room.
Resilient Sync Engine: Automatically detects network restoration via `NetworkMonitor` and triggers `SyncWorker` background sync via `WorkManager`.
Duplicate Submission Prevention: Client-side UUID generation paired with Room `OnConflictStrategy.REPLACE` and timestamp-based conflict resolution (`updatedAt`).
Secure Session Auth: Automatically attaches JWT bearer tokens via `AuthInterceptor` and triggers auto-logout on HTTP 401 Unauthorized responses.
Proof-of-Work Media: Attaches precise GPS coordinates and photo paths to completed tasks.

---

##  Getting Started

### Prerequisites
Android Studio: Ladybug (2024.2.1+) or newer.
JDK: Version 17.
Minimum SDK: API 24 (Android 7.0).
Target SDK: API 34 / 35.

## Launching App

Clone the repository in Android studio using this url : https://github.com/Arunav1070/Offline-First-Field-Operations-App.git

Open Project: Launch Android Studio and open the project directory.

Gradle Sync: Let Android Studio download and resolve dependencies.

Run App: Select an Emulator (API 26+) or connected physical device and press Run (Shift + F10).

## Testing

Unit Tests: Located under app/src/androidTest/java/data. Tests Room DAO operations using in-memory databases.
