# 📦 JitPack Publishing Guide - Timber KMP

This document explains how to publish and use Timber KMP through JitPack.

## 🚀 For Library Maintainers: Publishing to JitPack

### Prerequisites
- GitHub repository with Timber KMP code
- JitPack configuration already in place (✅ Done!)

### Step 1: Create a GitHub Release

1. Go to your GitHub repository
2. Click on "Releases" → "Create a new release"
3. Create a new tag (e.g., `v1.0.0`, `v1.1.0`)
4. Fill in release title and description
5. Click "Publish release"

### Step 2: JitPack Builds Automatically

JitPack will automatically:
- Detect the new release
- Clone the repository
- Build all KMP targets (Android, iOS, JVM, JS)
- Publish artifacts to JitPack's Maven repository

### Step 3: Check Build Status

Visit: `https://jitpack.io/#ocnyang/timber-kmp`

You'll see:
- ✅ Build status for each release
- 📦 Available artifacts
- 📝 Build logs (if needed)

---

## 📱 For Library Users: How to Use Timber KMP from JitPack

### Android Project (Gradle)

#### Step 1: Add JitPack repository

In your **root** `build.gradle` or `settings.gradle`:

```gradle
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }  // Add this line
    }
}
```

Or in older projects (`build.gradle`):

```gradle
allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

#### Step 2: Add dependency

In your app module `build.gradle`:

```gradle
dependencies {
    // For Android-only projects
    implementation 'com.github.ocnyang:timber-kmp:v1.0.0'

    // Or for specific platform
    implementation 'com.github.ocnyang.timber-kmp:timber-android:v1.0.0'
}
```

### Kotlin Multiplatform Project

In your shared module `build.gradle.kts`:

```kotlin
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                // Multiplatform artifact (includes all platforms)
                implementation("com.github.ocnyang:timber-kmp:v1.0.0")
            }
        }
    }
}
```

### Version Formats

JitPack supports multiple version formats:

```gradle
// Latest release
implementation 'com.github.ocnyang:timber-kmp:latest.release'

// Specific release tag
implementation 'com.github.ocnyang:timber-kmp:v1.0.0'

// Specific commit
implementation 'com.github.ocnyang:timber-kmp:abc1234'

// Specific branch (for testing)
implementation 'com.github.ocnyang:timber-kmp:main-SNAPSHOT'
```

---

## 🎯 Platform-Specific Artifacts

JitPack publishes separate artifacts for each platform:

| Platform | Artifact ID | Usage |
|----------|-------------|-------|
| **Common (All)** | `timber-kmp` | For KMP projects |
| **Android** | `timber-kmp:timber-android` | Android-only projects |
| **iOS** | `timber-kmp:timber-iosx64` | iOS x64 simulator |
| **iOS** | `timber-kmp:timber-iosarm64` | iOS ARM64 devices |
| **iOS** | `timber-kmp:timber-iossimulatorarm64` | iOS ARM64 simulator (M1/M2) |
| **JVM** | `timber-kmp:timber-jvm` | JVM/Desktop projects |
| **JS** | `timber-kmp:timber-js` | JavaScript projects |

### Example: iOS-only project

```gradle
dependencies {
    implementation 'com.github.ocnyang.timber-kmp:timber-iosx64:v1.0.0'
    implementation 'com.github.ocnyang.timber-kmp:timber-iosarm64:v1.0.0'
    implementation 'com.github.ocnyang.timber-kmp:timber-iossimulatorarm64:v1.0.0'
}
```

### Example: JVM-only project

```gradle
dependencies {
    implementation 'com.github.ocnyang.timber-kmp:timber-jvm:v1.0.0'
}
```

---

## 🔧 JitPack Configuration Details

### Files Configured

1. **`jitpack.yml`** - JitPack build configuration
   - JDK 17 for building
   - Skips tests and lint for faster builds
   - Publishes to Maven local

2. **`gradle.properties`** - Maven publishing metadata
   - Group ID: `com.jakewharton.timber`
   - Updated description with KMP support
   - Android SDK compatibility settings

3. **`timber/build.gradle`** - Maven publishing configuration
   - Configures all KMP publications
   - Adds Javadoc artifacts
   - Sets up local Maven repository

### How JitPack Uses These Files

```
1. JitPack clones your repo
2. Reads jitpack.yml for build instructions
3. Runs: ./gradlew :timber:publishToMavenLocal -x test -x lint
4. Collects all published artifacts
5. Hosts them on JitPack's Maven repository
```

---

## ✅ Verification

### Test Your JitPack Build

Before creating a release, test with a commit hash:

```gradle
implementation 'com.github.ocnyang:timber-kmp:abc1234'
```

Then sync Gradle. If it works, your JitPack configuration is correct!

### Check JitPack Build Logs

Visit: `https://jitpack.io/#ocnyang/timber-kmp`

- Click on the version you want to check
- View the full build log
- Debug any build issues

---

## 🎓 Best Practices

### For Maintainers

1. **Use Semantic Versioning**
   - `v1.0.0`, `v1.0.1`, `v1.1.0`, `v2.0.0`
   - Prefix with `v` (recommended)

2. **Test Before Release**
   - Test with commit hashes first
   - Verify all platforms build successfully

3. **Keep Dependencies Minimal**
   - JitPack has limited build time
   - Minimize external dependencies

4. **Document Breaking Changes**
   - Clear changelog in GitHub releases
   - Migration guides for major versions

### For Users

1. **Pin Specific Versions**
   - ❌ `implementation 'com.github.USER:repo:main-SNAPSHOT'`
   - ✅ `implementation 'com.github.USER:repo:v1.0.0'`

2. **Check JitPack Status**
   - Verify build succeeded before using
   - Check build logs if issues occur

3. **Use Platform-Specific Artifacts**
   - More efficient for single-platform projects
   - Reduces dependency size

---

## 📊 Example Release Process

### Step-by-Step Example

1. **Prepare code**
   ```bash
   git checkout main
   git pull origin main
   ```

2. **Update version** (if needed in gradle.properties)
   ```properties
   VERSION_NAME=1.0.0
   ```

3. **Commit and tag**
   ```bash
   git add .
   git commit -m "Release v1.0.0"
   git tag v1.0.0
   git push origin main --tags
   ```

4. **Create GitHub Release**
   - Go to GitHub → Releases → "Create a new release"
   - Tag: `v1.0.0`
   - Title: `Timber KMP v1.0.0`
   - Description: Changelog and features

5. **Wait for JitPack**
   - Visit: `https://jitpack.io/#ocnyang/timber-kmp`
   - Click "Get it" on the new version
   - Wait for build to complete (5-10 minutes)

6. **Verify**
   - Check build status is ✅
   - Test in a sample project

---

## 🆘 Troubleshooting

### Build Failed on JitPack

1. **Check build logs** on JitPack website
2. **Common issues:**
   - Missing Android SDK → Add `android.suppressUnsupportedCompileSdk` (✅ Already added)
   - JDK version mismatch → Specified in `jitpack.yml` (✅ Already set)
   - Test failures → We skip tests with `-x test` (✅ Already configured)

### Cannot Resolve Dependency

```
Could not find com.github.USER:timber-kmp:v1.0.0
```

**Solutions:**
1. Check JitPack repository is added to `settings.gradle` or `build.gradle`
2. Verify the tag exists on GitHub
3. Check JitPack build completed successfully
4. Try invalidating Gradle cache: `./gradlew clean --refresh-dependencies`

### Wrong Platform Artifact

If you get platform mismatch errors:

```gradle
// ❌ Wrong for iOS project
implementation 'com.github.USER.timber-kmp:timber-android:v1.0.0'

// ✅ Correct for iOS project
implementation 'com.github.USER.timber-kmp:timber-iosarm64:v1.0.0'
```

---

## 📝 Summary

### ✅ What's Configured

- [x] JitPack build configuration (`jitpack.yml`)
- [x] Maven publishing setup (`timber/build.gradle`)
- [x] Gradle properties for JitPack (`gradle.properties`)
- [x] All KMP targets enabled (Android, iOS, JVM, JS)

### 🚀 You Can Now

1. Create GitHub releases (e.g., `v1.0.0`)
2. JitPack will automatically build all platforms
3. Users can add your library via JitPack's Maven repository
4. Support all 7 platform targets out of the box

### 📖 Next Steps

1. Push your code to GitHub
2. Create your first release
3. Share JitPack link with users: `https://jitpack.io/#ocnyang/timber-kmp`

---

**Need help?**
- JitPack Docs: https://jitpack.io/docs/
- JitPack Support: https://github.com/jitpack/jitpack.io/issues

**Your JitPack URL:** `https://jitpack.io/#ocnyang/timber-kmp`

---

*Generated for Timber KMP - Multiplatform Logging Library*
