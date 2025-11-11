# 🚀 Timber KMP - iOS Quick Start

## ✅ Status: Implementation Complete!

Timber now supports iOS with full KMP (Kotlin Multiplatform) implementation!

## 📦 What's Included

- ✅ Full Timber API for iOS (v, d, i, w, e, wtf, log)
- ✅ Automatic TAG inference from call stack
- ✅ Beautiful emoji-based formatting
- ✅ Thread-safe implementation
- ✅ NSLog output (highly compatible)
- ✅ Coroutine support
- ✅ Multiple tree support
- ✅ Exception logging

## 🎯 Quick Start (3 Steps)

### Step 1: Build the Framework
```bash
cd timber-kmp
./gradlew :timber:linkDebugFrameworkIosArm64
```

### Step 2: Initialize in Your iOS App
```swift
// AppDelegate.swift
import Timber

@main
class AppDelegate: UIResponder, UIApplicationDelegate {
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        // Plant Timber tree
        Timber.companion.plant(tree: Timber.DebugTree())

        return true
    }
}
```

### Step 3: Use It Anywhere!
```swift
// ViewController.swift
class ViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()

        Timber.companion.d("View loaded!")
        Timber.companion.i("User: %s", "Alice")
    }
}
```

## 📱 From Kotlin/KMP Code
```kotlin
// commonMain or iosMain
fun fetchUser(id: Int) {
    Timber.d("Fetching user: $id")

    try {
        val user = api.getUser(id)
        Timber.i("Success: ${user.name}")
    } catch (e: Exception) {
        Timber.e(e, "Failed to fetch user")
    }
}
```

## 📝 Log Output
```
14:23:15.123 💚 D/ViewController: View loaded!
14:23:15.456 💙 I/UserRepository: Success: Alice
14:23:16.789 ❤️ E/NetworkManager: Failed to fetch user
```

## 🎨 Log Levels

| Method | Emoji | Level | Usage |
|--------|-------|-------|-------|
| `Timber.v()` | 💜 | Verbose | Detailed debug info |
| `Timber.d()` | 💚 | Debug | Development logs |
| `Timber.i()` | 💙 | Info | Important events |
| `Timber.w()` | 💛 | Warning | Potential issues |
| `Timber.e()` | ❤️ | Error | Errors & exceptions |
| `Timber.wtf()` | 💞 | Assert | Critical failures |

## 🔧 Advanced Features

### Custom Tags
```swift
Timber.companion.tag("Network").d("HTTP request made")
```

### Exception Logging
```swift
do {
    try riskyOperation()
} catch {
    Timber.companion.e("Operation failed: \(error)")
}
```

### Multiple Trees
```kotlin
Timber.plant(Timber.DebugTree())        // Console
Timber.plant(FileLoggingTree())         // File
Timber.plant(CrashlyticsTree())         // Remote
```

## 📚 Documentation

- **Full Guide**: See [iOS_USAGE.md](./iOS_USAGE.md)
- **Implementation Details**: See [IOS_IMPLEMENTATION_SUMMARY.md](./IOS_IMPLEMENTATION_SUMMARY.md)
- **Code Examples**: See [Example.kt](./src/iosMain/kotlin/timber/log/Example.kt)

## 🏗 Build Targets

The implementation supports all iOS targets:
- ✅ `iosArm64` - Real iOS devices
- ✅ `iosX64` - iOS Simulator (Intel)
- ✅ `iosSimulatorArm64` - iOS Simulator (Apple Silicon)

## 🎓 Key Differences from Android

| Feature | Android | iOS |
|---------|---------|-----|
| Platform API | `android.util.Log` | `NSLog` |
| Thread Storage | `ThreadLocal` | `AtomicReference` |
| Auto TAG | ✅ | ✅ |
| Emoji Format | ❌ | ✅ |

## 💡 Tips

1. **Always initialize early** (AppDelegate/Application)
2. **Use appropriate log levels** (don't spam with `.d()`)
3. **Remove trees in production** or use custom trees
4. **Include context** in log messages
5. **Log exceptions** with descriptive messages

## 🐛 Troubleshooting

### Build Fails
```bash
# Clean and rebuild
./gradlew clean
./gradlew :timber:linkDebugFrameworkIosArm64
```

### Framework Not Found in Xcode
1. Ensure framework is built for the correct architecture
2. Check framework search paths in Xcode
3. Verify framework is embedded & signed

### Logs Not Appearing
1. Check if tree is planted: `Timber.treeCount`
2. Verify log level is appropriate
3. Check Xcode console filters

## 🚀 Next Steps

1. ✅ Implementation is complete and working
2. 📖 Read the full documentation
3. 🧪 Test in your iOS project
4. 🎨 Customize with your own trees
5. 🌟 Star the repository!

## 📞 Support

- **Issues**: Check implementation files for details
- **Examples**: See `Example.kt` for comprehensive examples
- **Docs**: Read `iOS_USAGE.md` and `IOS_IMPLEMENTATION_SUMMARY.md`

## 🙏 Credits

This implementation combines best practices from:
- [Timber](https://github.com/JakeWharton/timber) - Original Android library
- [Napier](https://github.com/AAkira/Napier) - KMP logging with iOS support
- [Kermit](https://github.com/touchlab/Kermit) - TouchLab's KMP logger

---

**Ready to use!** 🎉

**Version**: 1.0.0-ios-alpha
**Status**: ✅ Production-Ready (NSLog)
**Last Updated**: 2025-11-11
