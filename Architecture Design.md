## Core Architectural Layers

## 1. UI Layer 
Jetpack Compose	
Declarative UI that renders state emitted by the ViewModel and captures user events.

## 2. State Layer 
TaskViewModel	
Manages UI state via StateFlow. Exposes business actions (create, complete, refresh) without holding direct UI references.

## 3. Domain / Repository 
TaskRepository
Mediates data traffic. Treats Room SQLite as the Single Source of Truth for all read operations.

## 4. Local Persistence
Room Database
Stores local task state, photo paths, and location coordinates. Writes update state immediately regardless of network connectivity.

## 5. Background Processing
WorkManager
Enqueues background sync requests (SyncWorker, PhotoUploadWorker) using network constraints and exponential backoff.

## 6. Background Processing
Retrofit & TokenManager
Intercepts requests via AuthInterceptor to inject JWT tokens and auto-clear sessions on HTTP 401 response codes. Token is securely held in EncryptedSharedPreferences.