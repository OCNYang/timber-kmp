![Timber](logo.png)

# 🌲 Timber - A logger with a small, extensible API

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-Multiplatform-blue?logo=kotlin" alt="Kotlin Multiplatform"/>
  <img src="https://img.shields.io/badge/Platform-Android%20%7C%20iOS%20%7C%20JVM%20%7C%20JS-green" alt="Platforms"/>
  <img src="https://img.shields.io/jitpack/version/com.github.ocnyang/timber-kmp?color=blue" alt="JitPack"/>
</p>

This is a logger with a small, extensible API which provides utility on top of Android's normal
`Log` class. **Now with full Kotlin Multiplatform support!**

I copy this class into all the little apps I make. I'm tired of doing it. Now it's a library.

Behavior is added through `Tree` instances. You can install an instance by calling `Timber.plant`.
Installation of `Tree`s should be done as early as possible. The `onCreate` of your application is
the most logical choice.

The `DebugTree` implementation will automatically figure out from which class it's being called and
use that class name as its tag. Since the tags vary, it works really well when coupled with a log
reader like [Pidcat][1].

There are no `Tree` implementations installed by default because every time you log in production, a
puppy dies.

## ✨ Platform Support

Timber now supports **all major Kotlin Multiplatform targets**:

| Platform | Status | Output | Auto TAG | Special Features |
|----------|--------|--------|----------|------------------|
| **Android** | ✅ | `android.util.Log` | ✅ | Original Timber API |
| **iOS** | ✅ | `NSLog` | ✅ | Emoji logging 💚 |
| **JVM** | ✅ | `System.out` / `Logger` | ✅ | Dual output modes |
| **JS** | ✅ | `console.*` | Browser | Browser & Node.js |

### iOS Targets
- ✅ `iosArm64` - Physical devices
- ✅ `iosX64` - Intel simulators
- ✅ `iosSimulatorArm64` - Apple Silicon simulators

### JS Targets
- ✅ Browser
- ✅ Node.js

## 🚀 Usage

Two easy steps:

 1. Install any `Tree` instances you want in the `onCreate` of your application class.
 2. Call `Timber`'s static methods everywhere throughout your app.

### Android
```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Timber.d("Application started!")
    }
}
```

### iOS (Swift)
```swift
import Timber

class AppDelegate: UIResponder, UIApplicationDelegate {
    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        Timber.companion.plant(tree: Timber.DebugTree())
        Timber.companion.d("Application started!")
        return true
    }
}
```

### JVM
```kotlin
fun main() {
    // Use System.out (default)
    Timber.plant(Timber.DebugTree())

    // Or use java.util.logging
    Timber.plant(Timber.DebugTree(useJavaLogging = true))

    Timber.d("Application started!")
}
```

### JavaScript
```javascript
// From JavaScript
timber.Timber.companion.plant(new timber.Timber.DebugTree());
timber.Timber.companion.d("Application started!");
```

### Common Kotlin (Multiplatform)
```kotlin
// In commonMain - works everywhere!
fun initLogging() {
    Timber.plant(Timber.DebugTree())
}

fun doSomething() {
    Timber.d("Debug message")
    Timber.i("Info message")
    Timber.w("Warning message")
    Timber.e("Error message")

    try {
        riskyOperation()
    } catch (e: Exception) {
        Timber.e(e, "Operation failed")
    }
}
```

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

### JavaScript (Browser Console)
```
[DEBUG] MainActivity: User logged in
[INFO] NetworkManager: Request successful
[ERROR] DatabaseHelper: Connection failed
```

Check out the sample app in `timber-sample/` to see it in action, or see platform-specific examples in [timber/src/iosMain/kotlin/timber/log/Example.kt](timber/src/iosMain/kotlin/timber/log/Example.kt).

## 📦 Download

### Kotlin Multiplatform (Recommended)

Add JitPack repository to your `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")  // Add this
    }
}
```

Then add Timber KMP to your shared module:

```kotlin
// shared/build.gradle.kts
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.github.ocnyang:timber-kmp:v1.0.0")
            }
        }
    }
}
```

### Android Only

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

// app/build.gradle.kts
dependencies {
    implementation("com.github.ocnyang:timber-kmp:v1.0.0")
}
```

### Gradle (Groovy)

```groovy
// settings.gradle
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}

// build.gradle
dependencies {
    implementation 'com.github.ocnyang:timber-kmp:v1.0.0'
}
```

### Platform-Specific Dependencies

If you only need a specific platform:

```kotlin
// iOS only
implementation("com.github.ocnyang.timber-kmp:timber-iosarm64:v1.0.0")

// JVM only
implementation("com.github.ocnyang.timber-kmp:timber-jvm:v1.0.0")

// JS only
implementation("com.github.ocnyang.timber-kmp:timber-js:v1.0.0")
```

### Original Android Version (Maven Central)

The original Android-only version is still available on Maven Central:

```groovy
dependencies {
  implementation 'com.jakewharton.timber:timber:5.0.1'
}
```

## 🎓 Documentation

- **[README_KMP.md](README_KMP.md)** - KMP overview and quick start
- **[QUICKSTART_iOS.md](QUICKSTART_iOS.md)** - iOS 5-minute guide
- **[iOS_USAGE.md](timber/iOS_USAGE.md)** - Complete iOS usage guide
- **[KMP_COMPLETE_SUMMARY.md](KMP_COMPLETE_SUMMARY.md)** - Full platform implementation details
- **[JITPACK_GUIDE.md](JITPACK_GUIDE.md)** - Publishing and usage guide for JitPack
- **Original Docs**: [jakewharton.github.io/timber](https://jakewharton.github.io/timber/docs/5.x/)

## 🌳 Custom Trees

Create custom `Tree` implementations that work across all platforms:

```kotlin
// Works on ALL platforms!
class CrashlyticsTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority >= LogLevel.ERROR) {
            // Send to crash reporting service
            crashlytics.recordException(t ?: Exception(message))
        }
    }
}

// Plant multiple trees
fun initLogging() {
    Timber.plant(Timber.DebugTree())    // Console output
    Timber.plant(CrashlyticsTree())     // Crash reporting
}
```

## 🔍 Lint

Timber ships with embedded lint rules to detect problems in your app (Android only).

 *  **TimberArgCount** (Error) - Detects an incorrect number of arguments passed to a `Timber` call for
    the specified format string.

        Example.java:35: Error: Wrong argument count, format string Hello %s %s! requires 2 but format call supplies 1 [TimberArgCount]
            Timber.d("Hello %s %s!", firstName);
            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 *  **TimberArgTypes** (Error) - Detects arguments which are of the wrong type for the specified format string.

        Example.java:35: Error: Wrong argument type for formatting argument '#0' in success = %b: conversion is 'b', received String (argument #2 in method call) [TimberArgTypes]
            Timber.d("success = %b", taskName);
                                     ~~~~~~~~
 *  **TimberTagLength** (Error) - Detects the use of tags which are longer than Android's maximum length of 23.

        Example.java:35: Error: The logging tag can be at most 23 characters, was 35 (TagNameThatIsReallyReallyReallyLong) [TimberTagLength]
            Timber.tag("TagNameThatIsReallyReallyReallyLong").d("Hello %s %s!", firstName, lastName);
            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 *  **LogNotTimber** (Warning) - Detects usages of Android's `Log` that should be using `Timber`.

        Example.java:35: Warning: Using 'Log' instead of 'Timber' [LogNotTimber]
            Log.d("Greeting", "Hello " + firstName + " " + lastName + "!");
                ~

 *  **StringFormatInTimber** (Warning) - Detects `String.format` used inside of a `Timber` call. Timber
    handles string formatting automatically.

        Example.java:35: Warning: Using 'String#format' inside of 'Timber' [StringFormatInTimber]
            Timber.d(String.format("Hello, %s %s", firstName, lastName));
                     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 *  **BinaryOperationInTimber** (Warning) - Detects string concatenation inside of a `Timber` call. Timber
    handles string formatting automatically and should be preferred over manual concatenation.

        Example.java:35: Warning: Replace String concatenation with Timber's string formatting [BinaryOperationInTimber]
            Timber.d("Hello " + firstName + " " + lastName + "!");
                     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 *  **TimberExceptionLogging** (Warning) - Detects the use of null or empty messages, or using the exception message
    when logging an exception.

        Example.java:35: Warning: Explicitly logging exception message is redundant [TimberExceptionLogging]
             Timber.d(e, e.getMessage());
                         ~~~~~~~~~~~~~~

## 🔗 Useful Links

- **JitPack Repository**: https://jitpack.io/#ocnyang/timber-kmp
- **Original Timber**: https://github.com/JakeWharton/timber
- **Pidcat** (colored logcat): http://github.com/JakeWharton/pidcat/

## 🎊 Credits

### Original Library
**[Timber](https://github.com/JakeWharton/timber)** by [@JakeWharton](https://github.com/JakeWharton)

### KMP Implementation Inspired By
- **[Napier](https://github.com/AAkira/Napier)** - iOS stack trace parsing, emoji formatting
- **[Kermit](https://github.com/touchlab/Kermit)** - OSLog infrastructure, architecture

### KMP Port Maintainer
[@ocnyang](https://github.com/ocnyang)

## 📊 Build Status

```
✅ Android        - BUILD SUCCESSFUL
✅ iOS Arm64      - BUILD SUCCESSFUL
✅ iOS X64        - BUILD SUCCESSFUL
✅ iOS SimArm64   - BUILD SUCCESSFUL
✅ JVM            - BUILD SUCCESSFUL
✅ JS (Browser)   - BUILD SUCCESSFUL
✅ JS (Node.js)   - BUILD SUCCESSFUL
```

**Total Platforms**: 7 targets
**Total Code**: ~2,600 lines

## 📄 License

```
Copyright 2025 Timber KMP Contributors
Copyright 2013 Jake Wharton

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

---

<p align="center">
  <strong>Made with ❤️ for the Kotlin Multiplatform community</strong><br/>
  <sub>Standing on the shoulders of giants: Timber, Napier, and Kermit</sub>
</p>

 [1]: http://github.com/JakeWharton/pidcat/
