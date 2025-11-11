# Timber for iOS - Usage Guide

## ✅ Implementation Status

Timber now supports iOS with full KMP implementation! This implementation combines the best practices from:
- **Napier**: Stack trace parsing and emoji-based formatting
- **Kermit**: OSLog support and clean architecture
- **Timber**: Simple, elegant API

## 📦 Features

### ✅ Implemented
- ✅ All Timber logging methods (v, d, i, w, e, wtf, log)
- ✅ Automatic TAG inference from call stack
- ✅ Beautiful emoji-based log formatting
- ✅ Thread-safe tree management with AtomicReference
- ✅ NSLog output (legacy, highly compatible)
- ✅ Coroutine support (with special tag formatting)
- ✅ Custom tag with `Timber.tag()`
- ✅ Multiple tree support
- ✅ Throwable/Exception logging

### 🚧 TODO
- ⏳ Full OSLog support (requires C interop setup)
- ⏳ iOS version detection for OSLog
- ⏳ Performance optimization

## 🚀 Usage

### Swift (From iOS App)

```swift
import Timber

// Initialize in AppDelegate
class AppDelegate: UIResponder, UIApplicationDelegate {
    func application(_ application: UIApplication,
                    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {

        // Plant debug tree for development
        Timber.companion.plant(tree: Timber.DebugTree())

        return true
    }
}

// Use anywhere in your Swift code
func someFunction() {
    Timber.companion.d("Hello from Swift!")
    Timber.companion.i("User logged in: %s", "Alice")
    Timber.companion.e("Network error occurred")
}
```

### Kotlin (From KMP Shared Code)

```kotlin
// commonMain or iosMain
class UserRepository {
    fun getUser(id: Int): User? {
        Timber.d("Fetching user: $id")

        return try {
            val user = api.getUser(id)
            Timber.i("User fetched successfully: ${user.name}")
            user
        } catch (e: Exception) {
            Timber.e(e, "Failed to fetch user")
            null
        }
    }
}

// iOS-specific initialization (iosMain)
fun initTimber() {
    if (BuildConfig.DEBUG) {
        // Use emoji-based NSLog (default)
        Timber.plant(Timber.DebugTree(useOSLog = false))

        // Or use OSLog (when implemented)
        // Timber.plant(Timber.DebugTree(useOSLog = true))
    }
}
```

## 📝 Log Output Format

### NSLog Output (Current Implementation)
```
14:23:15.123 💚 D/MainActivity: User logged in
14:23:15.456 💙 I/NetworkManager: Request completed
14:23:16.789 ❤️ E/DatabaseHelper: Connection failed
```

### Legend
- 💜 V - Verbose
- 💚 D - Debug
- 💙 I - Info
- 💛 W - Warning
- ❤️ E - Error
- 💞 A - Assert

## 🎯 Advanced Usage

### Custom Tags
```kotlin
Timber.tag("CustomTag").d("This message has a custom tag")
// Output: 14:23:15.123 💚 D/CustomTag: This message has a custom tag
```

### Multiple Trees
```kotlin
// Plant multiple trees for different purposes
Timber.plant(Timber.DebugTree())  // Console output
Timber.plant(FileLoggingTree())   // File output (implement yourself)
Timber.plant(CrashlyticsTree())   // Crashlytics (implement yourself)

// All trees receive the same log messages
Timber.d("This goes to all trees!")
```

### Exception Logging
```kotlin
try {
    riskyOperation()
} catch (e: Exception) {
    Timber.e(e, "Operation failed")
    // Automatically includes stack trace
}
```

### Coroutine Support
The implementation automatically detects coroutine calls and formats them nicely:
```kotlin
suspend fun fetchData() {
    Timber.d("Fetching data in coroutine")
    // Output: 14:23:15.123 💚 D/MainKt[async]: Fetching data in coroutine
}
```

## 🏗 Implementation Details

### Architecture
- Uses `expect`/`actual` pattern for KMP
- `AtomicReference` instead of `ThreadLocal` for iOS thread safety
- Borrowed stack trace parsing from Napier
- OSLog infrastructure inspired by Kermit

### Stack Trace Parsing
The implementation uses `NSThread.callStackSymbols()` to automatically infer the TAG:
- Works with regular classes
- Handles anonymous classes ($1, $2, etc.)
- Special formatting for coroutines
- Removes method parameters for cleaner tags

### Thread Safety
- Uses `AtomicReference` for managing trees list
- Safe for use from any thread/queue
- No locking required for logging operations

## 🔧 Build Configuration

The iOS targets are automatically configured:
```gradle
// build.gradle
kotlin {
    iosX64()
    iosArm64()
    iosSimulatorArm64()
}
```

## 📱 Xcode Integration

### Framework Setup
1. Build the framework:
   ```bash
   ./gradlew :timber:linkDebugFrameworkIosArm64
   ```

2. Add framework to Xcode project:
   - Drag `Timber.framework` to your Xcode project
   - Embed & Sign the framework

3. Import in Swift:
   ```swift
   import Timber
   ```

### Debug vs Release
```swift
#if DEBUG
    Timber.companion.plant(tree: Timber.DebugTree())
#else
    // Production: no logging or custom tree
    // Timber.companion.plant(tree: ProductionTree())
#endif
```

## 🎨 Comparison with Android

| Feature | Android | iOS (This Impl) |
|---------|---------|-----------------|
| Basic Logging | ✅ | ✅ |
| Auto TAG | ✅ | ✅ |
| ThreadLocal | ✅ | ❌ (AtomicReference) |
| Platform Log | android.util.Log | NSLog |
| Advanced Log | - | OSLog (TODO) |
| Emoji Format | ❌ | ✅ |
| Performance | Excellent | Good |

## 🐛 Known Limitations

1. **OSLog Support**: Currently uses NSLog. Full OSLog support requires C interop
2. **Format Args**: Basic format support (not full printf-style)
3. **Version Detection**: iOS version check not implemented yet
4. **File/Line Numbers**: Not included in tags (iOS limitation)

## 🚀 Future Improvements

1. ✨ Full OSLog C interop implementation
2. ✨ iOS version detection for conditional OSLog
3. ✨ File/line number support via compiler tricks
4. ✨ Performance profiling and optimization
5. ✨ SwiftUI-specific trees (e.g., OSLog categories)

## 📚 Resources

- [Timber Android](https://github.com/JakeWharton/timber)
- [Napier (KMP Logging)](https://github.com/AAkira/Napier)
- [Kermit (TouchLab)](https://github.com/touchlab/Kermit)
- [Apple's Logging](https://developer.apple.com/documentation/os/logging)

## 💡 Tips

1. **Always plant trees early** in app lifecycle (AppDelegate)
2. **Use different trees** for Debug vs Release builds
3. **Keep log messages concise** for better Xcode console readability
4. **Use appropriate log levels** (don't abuse `.d()` for everything)
5. **Remove trees** when no longer needed with `uprootAll()`

## 📞 Support

For issues or questions:
- Check existing iOS tests in the project
- Review the implementation in `iosMain/kotlin/timber/log/Timber.kt`
- Compare with Napier/Kermit implementations for inspiration

---

**Status**: ✅ Ready for use in development
**Version**: 1.0.0-ios-alpha
**Last Updated**: 2025-11-11
