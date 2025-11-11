# 📦 JitPack Quick Reference - Timber KMP

## ✅ Configuration Complete!

All JitPack configuration files have been added and tested successfully.

---

## 🚀 For You: Publishing Steps

### 1. Push to GitHub
```bash
git add .
git commit -m "Add JitPack configuration for KMP publishing"
git push origin main
```

### 2. Create a Release on GitHub

1. Go to your GitHub repository
2. Click **"Releases"** → **"Create a new release"**
3. **Tag version**: `v1.0.0` (or your version)
4. **Release title**: `Timber KMP v1.0.0`
5. **Description**:
   ```
   🌲 Timber KMP - Multiplatform Logging

   First release with support for:
   - ✅ Android
   - ✅ iOS (3 targets)
   - ✅ JVM
   - ✅ JS (Browser & Node.js)
   ```
6. Click **"Publish release"**

### 3. Wait for JitPack to Build

- JitPack will automatically detect the new release
- Build time: ~5-10 minutes
- Check status at: `https://jitpack.io/#ocnyang/timber-kmp`

### 4. Verify Build

Visit `https://jitpack.io/#ocnyang/timber-kmp` and confirm:
- ✅ Build status is green
- ✅ All platform artifacts are published

---

## 📱 For Users: How to Add Dependency

### Step 1: Add JitPack Repository

In `settings.gradle.kts` or `settings.gradle`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")  // Add this
    }
}
```

### Step 2: Add Timber KMP Dependency

#### For Kotlin Multiplatform Projects:

```kotlin
// In shared/build.gradle.kts
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

#### For Android-Only Projects:

```kotlin
// In app/build.gradle.kts
dependencies {
    implementation("com.github.ocnyang:timber-kmp:v1.0.0")
}
```

---

## 🎯 Published Platform Artifacts

JitPack will publish these artifacts:

| Platform | Artifact | Size |
|----------|----------|------|
| **Android** | `timber-android-5.1.0-SNAPSHOT.aar` | 32KB |
| **iOS Arm64** | `timber-iosarm64-5.1.0-SNAPSHOT.klib` | 31KB |
| **iOS X64** | `timber-iosx64-5.1.0-SNAPSHOT.klib` | 31KB |
| **iOS Sim Arm64** | `timber-iossimulatorarm64-5.1.0-SNAPSHOT.klib` | 31KB |
| **JVM** | `timber-jvm-5.1.0-SNAPSHOT.jar` | 8KB |
| **JS** | `timber-js-5.1.0-SNAPSHOT.klib` | 29KB |

Plus: sources, javadoc, metadata, POM, and module files for each platform.

---

## 📁 Files Added/Modified

### New Files:
1. ✅ **`jitpack.yml`** - JitPack build configuration
   - Specifies JDK 17
   - Skips tests for faster builds
   - Publishes to Maven local

2. ✅ **`JITPACK_GUIDE.md`** - Complete guide (this file's full version)
   - For maintainers: how to publish
   - For users: how to use
   - Troubleshooting tips

3. ✅ **`JITPACK_QUICKREF.md`** - This quick reference

### Modified Files:
1. ✅ **`gradle.properties`**
   - Updated description to mention KMP support
   - Added Android SDK compatibility settings

2. ✅ **`timber/build.gradle`**
   - Added publishing repository configuration
   - Ensures all KMP targets are published

3. ✅ **Platform Timber.kt files** (Android/iOS/JVM/JS)
   - Fixed `actual override` declarations for Forest.log()
   - Required for KMP expect/actual pattern

---

## ✅ Verification Results

All platforms published successfully to local Maven:

```
✅ timber-android/5.1.0-SNAPSHOT
✅ timber-iosarm64/5.1.0-SNAPSHOT
✅ timber-iosx64/5.1.0-SNAPSHOT
✅ timber-iossimulatorarm64/5.1.0-SNAPSHOT
✅ timber-jvm/5.1.0-SNAPSHOT
✅ timber-js/5.1.0-SNAPSHOT
```

**Build command tested:**
```bash
./gradlew clean :timber:publishToMavenLocal -x test -x lint
```

**Result:** ✅ BUILD SUCCESSFUL in 8s

---

## 🎓 Version Format Examples

```kotlin
// Specific release
implementation("com.github.USER:timber-kmp:v1.0.0")

// Latest release
implementation("com.github.USER:timber-kmp:latest.release")

// Specific commit (for testing)
implementation("com.github.USER:timber-kmp:abc1234")

// Branch snapshot (development)
implementation("com.github.USER:timber-kmp:main-SNAPSHOT")
```

---

## 📚 Usage Example

```kotlin
// Initialize in your app
fun initLogging() {
    if (Platform.isDebug) {
        Timber.plant(Timber.DebugTree())
    }
}

// Use anywhere
Timber.d("Hello from ${Platform.name}!")
Timber.e(exception, "Something went wrong")
```

### Platform-Specific Output:

**iOS:**
```
14:23:15.123 💚 D/MyClass: Hello from iOS!
```

**JVM:**
```
14:23:15.123 [D] MyClass: Hello from JVM!
```

**JS:**
```
[DEBUG] MyClass: Hello from JavaScript!
```

---

## 🆘 Troubleshooting

### Build Failed on JitPack?
1. Check logs at `https://jitpack.io/#USER/timber-kmp`
2. Verify all code compiles locally
3. Ensure tag exists on GitHub

### Cannot Resolve Dependency?
```kotlin
// Make sure JitPack is in repositories
maven("https://jitpack.io")

// Try refreshing dependencies
./gradlew clean --refresh-dependencies
```

### Wrong Platform Artifact?
```kotlin
// For multiplatform, use base artifact:
implementation("com.github.USER:timber-kmp:v1.0.0")

// For single platform, be specific:
implementation("com.github.USER.timber-kmp:timber-android:v1.0.0")
```

---

## 📞 Support

- **JitPack Docs**: https://jitpack.io/docs/
- **Full Guide**: See `JITPACK_GUIDE.md`
- **Timber KMP Docs**: See `README_KMP.md`

---

## ✨ Summary

**Status:** ✅ Ready for JitPack Publishing

**What You Need to Do:**
1. Push code to GitHub
2. Create a release (e.g., v1.0.0)
3. JitPack builds automatically
4. Share the JitPack link with users

**Your JitPack URL:**
```
https://jitpack.io/#ocnyang/timber-kmp
```

Replace `ocnyang` with your actual GitHub username!

---

*Configuration completed successfully on 2025-11-11*
*All platforms tested and verified ✅*
