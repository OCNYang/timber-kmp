# 🌲 Timber KMP - Multiplatform Logging Made Easy

![Timber](logo.png)

<p align="center">
  <strong>Timber's elegant logging API, now available across ALL Kotlin Multiplatform targets!</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-Multiplatform-blue?logo=kotlin" alt="Kotlin Multiplatform"/>
  <img src="https://img.shields.io/badge/Platform-Android%20%7C%20iOS%20%7C%20JVM%20%7C%20JS-green" alt="Platforms"/>
  <img src="https://img.shields.io/badge/Status-Production%20Ready-success" alt="Status"/>
</p>

---

## ✨ What's New - KMP Support!

This is a **Kotlin Multiplatform** port of Jake Wharton's excellent Timber logging library, now supporting:

- ✅ **Android** - Original implementation
- ✅ **iOS** (3 targets) - iPhone, Simulator Intel & ARM
- ✅ **JVM** - Desktop & Server applications
- ✅ **JS** (2 targets) - Browser & Node.js

**Same simple API, works everywhere!**

## 🚀 Quick Start

### Android (Original)
```kotlin
Timber.plant(Timber.DebugTree())
Timber.d("Hello, Timber!")
```

### iOS (Swift)
```swift
Timber.companion.plant(tree: Timber.DebugTree())
Timber.companion.d("Hello from iOS!")
```

### JVM
```kotlin
Timber.plant(Timber.DebugTree())
Timber.d("Hello from JVM!")
```

### JavaScript
```javascript
timber.Timber.companion.plant(new timber.Timber.DebugTree());
timber.Timber.companion.d("Hello from JS!");
```

## 📱 Platform Support Matrix

| Platform | Status | Output | Auto TAG | Special Features |
|----------|--------|--------|----------|------------------|
| **Android** | ✅ | `android.util.Log` | ✅ | Original Timber |
| **iOS** | ✅ | `NSLog` | ✅ | Emoji logging 💚 |
| **JVM** | ✅ | `System.out` / `Logger` | ✅ | Dual output modes |
| **JS** | ✅ | `console.*` | ❌ | Browser & Node.js |

## 📝 Log Output Examples

### iOS
```
14:23:15.123 💚 D/MainActivity: User logged in
14:23:15.456 💙 I/NetworkManager: Request successful
14:23:16.789 ❤️ E/DatabaseHelper: Connection failed
```

### JVM
```
14:23:15.123 [D] MainActivity: User logged in
14:23:15.456 [I] NetworkManager: Request successful
14:23:16.789 [E] DatabaseHelper: Connection failed
```

### JavaScript
```
[DEBUG] MainActivity: User logged in
[INFO] NetworkManager: Request successful
[ERROR] DatabaseHelper: Connection failed
```

## 🌳 Custom Trees (Cross-Platform)

```kotlin
// Works on ALL platforms!
class RemoteLoggingTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority >= LogLevel.ERROR) {
            // Send to your logging service
            sendToServer(message)
        }
    }
}

Timber.plant(Timber.DebugTree())    // Console
Timber.plant(RemoteLoggingTree())   // Remote
```

## 📚 Documentation

- **[Android Original](https://github.com/JakeWharton/timber)** - Original Timber documentation
- **[iOS Usage Guide](timber/iOS_USAGE.md)** - iOS-specific features
- **[Complete KMP Summary](KMP_COMPLETE_SUMMARY.md)** - All platforms overview
- **[Quick Start iOS](QUICKSTART_iOS.md)** - 5-minute iOS guide

## 🎓 Credits

### Original Library
**[Timber](https://github.com/JakeWharton/timber)** by [@JakeWharton](https://github.com/JakeWharton)

### KMP Implementation Inspired By
- **[Napier](https://github.com/AAkira/Napier)** - iOS stack trace parsing, emoji formatting
- **[Kermit](https://github.com/touchlab/Kermit)** - OSLog infrastructure, architecture

## 📊 Build Status

```bash
✅ Android    - BUILD SUCCESSFUL
✅ iOS Arm64  - BUILD SUCCESSFUL
✅ iOS X64    - BUILD SUCCESSFUL
✅ iOS SimArm64 - BUILD SUCCESSFUL
✅ JVM        - BUILD SUCCESSFUL
✅ JS         - BUILD SUCCESSFUL
```

## 📄 License

```
Copyright 2025 Timber KMP Contributors
Copyright 2013 Jake Wharton

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
```

## 🤝 Contributing

Contributions welcome! See [KMP_COMPLETE_SUMMARY.md](KMP_COMPLETE_SUMMARY.md) for implementation details.

---

<p align="center">
  <strong>Made with ❤️ for the Kotlin Multiplatform community</strong><br/>
  <sub>Standing on the shoulders of giants: Timber, Napier, and Kermit</sub>
</p>
