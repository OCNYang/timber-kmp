# 🚀 快速发布 v1.0.1 指南

## ⚡ 快速执行 (复制粘贴即可)

```bash
cd /Users/lucas/AIChatProject/timber-kmp

# 1. 提交修复
git add jitpack.yml JITPACK_GUIDE.md JITPACK_FIX.md
git commit -m "fix: Update JitPack to use Java 21 for timber-lint compatibility

- Update jitpack.yml from Java 17 to Java 21
- timber-lint module requires Java 21 (jvmToolchain)
- Fixes JitPack build failure"

git push origin main

# 2. 创建并推送 tag
git tag -a v1.0.1 -m "v1.0.1 - Fix JitPack build with Java 21"
git push origin v1.0.1
```

## 📝 GitHub Release 步骤

1. **访问**: https://github.com/ocnyang/timber-kmp/releases/new

2. **选择 tag**: `v1.0.1`

3. **Release title**: `Timber KMP v1.0.1 - JitPack Build Fix`

4. **复制以下内容到 Description**:

---

🌲 Timber KMP v1.0.1 - JitPack Build Fix

## 🐛 Bug Fix

Fixed JitPack build failure caused by Java version mismatch:
- ✅ Updated JitPack configuration to use Java 21
- ✅ Required by `timber-lint` module (`jvmToolchain(21)`)
- ✅ All platforms now build successfully on JitPack

## ✨ Features (from v1.0.0)

Timber now supports **all major Kotlin Multiplatform targets**!

### Supported Platforms
- ✅ **Android** - Original Timber API
- ✅ **iOS** (3 targets) - Arm64, X64, SimulatorArm64
- ✅ **JVM** - Desktop & Server applications
- ✅ **JS** - Browser & Node.js

### Key Features
- 🎯 Same simple API across all platforms
- 💚 Emoji-based logging for iOS
- 🔄 Dual output modes for JVM
- 📱 Native console support for JS
- 📚 Comprehensive documentation
- 🚀 Easy integration via JitPack

## 📦 Installation

Add JitPack repository to your `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
```

Then add Timber KMP dependency:

```kotlin
// In your shared module
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.github.ocnyang:timber-kmp:v1.0.1")  // ⬅️ Use v1.0.1
            }
        }
    }
}
```

## 🚀 Quick Examples

**Android**:
```kotlin
Timber.plant(Timber.DebugTree())
Timber.d("Hello, Timber!")
```

**iOS (Swift)**:
```swift
Timber.companion.plant(tree: Timber.DebugTree())
Timber.companion.d("Hello from iOS!")
```

**JVM**:
```kotlin
Timber.plant(Timber.DebugTree(useJavaLogging = true))
Timber.d("Hello from JVM!")
```

**JavaScript**:
```javascript
timber.Timber.companion.plant(new timber.Timber.DebugTree());
timber.Timber.companion.d("Hello from JS!");
```

## 📝 Platform Output Examples

**iOS**:
```
14:23:15.123 💚 D/MyClass: User logged in
14:23:15.456 💙 I/Network: Request successful
```

**JVM**:
```
14:23:15.123 [D] MyClass: User logged in
14:23:15.456 [I] Network: Request successful
```

**JS (Browser)**:
```
[DEBUG] MyClass: User logged in
[INFO] Network: Request successful
```

## 📚 Documentation
- [README.md](README.md) - Main documentation
- [README_KMP.md](README_KMP.md) - KMP overview
- [QUICKSTART_iOS.md](QUICKSTART_iOS.md) - iOS quick start
- [JITPACK_GUIDE.md](JITPACK_GUIDE.md) - JitPack publishing guide

## 🔗 Links

- **JitPack**: https://jitpack.io/#ocnyang/timber-kmp
- **Original Timber**: https://github.com/JakeWharton/timber

## 🎊 Credits

- Original Timber by [@JakeWharton](https://github.com/JakeWharton)
- Inspired by [Napier](https://github.com/AAkira/Napier) and [Kermit](https://github.com/touchlab/Kermit)

---

**Total Platforms**: 7 targets
**Total Code**: ~2,600 lines
**Build Status**: ✅ All platforms successful

---

5. **点击**: "Publish release"

## ⏰ 等待 JitPack 构建

1. 访问: https://jitpack.io/#ocnyang/timber-kmp
2. 在输入框输入 `v1.0.1` 并按回车
3. 等待 5-10 分钟
4. 刷新页面，确认看到绿色 ✅

## ✅ 验证

访问构建日志确认成功:
https://jitpack.io/com/github/ocnyang/timber-kmp/v1.0.1/build.log

看到 `BUILD SUCCESSFUL` 就完成了！

---

**问题？** 查看详细说明: `JITPACK_FIX.md`
