import timber.log.Timber
import timber.log.LogLevel

/**
 * Example iOS usage of Timber KMP
 *
 * This file demonstrates how to use Timber in iOS/KMP projects.
 */

// ============================================
// 1. Initialization (call from iOS app startup)
// ============================================

fun initializeTimberForIOS() {
    // For debug builds
    Timber.plant(Timber.DebugTree(
        useOSLog = false,  // Use NSLog for now
        defaultTag = "MyApp"
    ))

    Timber.d("Timber initialized for iOS!")
}

// ============================================
// 2. Basic Logging Examples
// ============================================

fun basicLoggingExamples() {
    // Different log levels
    Timber.v("This is a verbose message")
    Timber.d("This is a debug message")
    Timber.i("This is an info message")
    Timber.w("This is a warning message")
    Timber.e("This is an error message")
    Timber.wtf("This is an assert message")

    // With format arguments (basic support)
    val username = "Alice"
    val userId = 123
    Timber.d("User logged in: %s with ID: %s", username, userId)
}

// ============================================
// 3. Real-World Usage in Repository
// ============================================

class UserRepository {
    fun getUser(id: Int): User? {
        Timber.d("Fetching user with ID: $id")

        return try {
            // Simulate API call
            val user = fetchUserFromApi(id)
            Timber.i("User fetched successfully: ${user.name}")
            user
        } catch (e: NetworkException) {
            Timber.e(e, "Network error while fetching user: $id")
            null
        } catch (e: Exception) {
            Timber.e(e, "Unexpected error")
            null
        }
    }

    fun deleteUser(id: Int): Boolean {
        Timber.d("Deleting user: $id")

        return try {
            deleteUserFromApi(id)
            Timber.i("User deleted successfully: $id")
            true
        } catch (e: Exception) {
            Timber.e(e, "Failed to delete user: $id")
            false
        }
    }

    private fun fetchUserFromApi(id: Int): User {
        // Simulated API call
        return User(id, "User $id", "user$id@example.com")
    }

    private fun deleteUserFromApi(id: Int) {
        // Simulated API call
    }
}

// ============================================
// 4. Custom Tags
// ============================================

fun customTagExample() {
    // One-time custom tag
    Timber.tag("Network").d("Making HTTP request")
    Timber.tag("Database").i("Query executed")
    Timber.tag("Analytics").w("Event not tracked")

    // Back to auto-generated tag
    Timber.d("This uses automatic tag inference")
}

// ============================================
// 5. Exception Logging
// ============================================

fun exceptionLoggingExample() {
    try {
        riskyOperation()
    } catch (e: IllegalArgumentException) {
        // Log exception with message
        Timber.e(e, "Invalid argument provided")
    } catch (e: Exception) {
        // Log exception only
        Timber.e(e)
    }
}

fun riskyOperation() {
    throw IllegalArgumentException("Something went wrong!")
}

// ============================================
// 6. Coroutine Support
// ============================================

suspend fun coroutineExample() {
    Timber.d("Starting async operation")

    // The tag will automatically show [async] for coroutines
    val result = fetchDataAsync()

    Timber.i("Async operation completed: $result")
}

suspend fun fetchDataAsync(): String {
    // Simulated async operation
    Timber.d("Fetching data asynchronously...")
    return "data"
}

// ============================================
// 7. Multiple Trees Example
// ============================================

fun multipleTreesExample() {
    // Plant multiple trees
    Timber.plant(Timber.DebugTree())

    // You can also create custom trees
    // Timber.plant(FileLoggingTree())
    // Timber.plant(CrashlyticsTree())

    // This log will go to all planted trees
    Timber.d("This message goes to all trees")

    // Remove a specific tree
    // Timber.uproot(fileLoggingTree)

    // Remove all trees
    // Timber.uprootAll()
}

// ============================================
// 8. Production Configuration
// ============================================

fun initializeForProduction() {
    // In production, you might want to:
    // 1. Only log errors
    // 2. Send logs to a remote service
    // 3. Not log at all

    // Example: Custom production tree
    class ProductionTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            // Only log errors in production
            if (priority >= LogLevel.ERROR) {
                // Send to crash reporting service
                // FirebaseCrashlytics.log(message)
                // if (t != null) FirebaseCrashlytics.recordException(t)
            }
        }
    }

    Timber.plant(ProductionTree())
}

// ============================================
// 9. Expected Log Output
// ============================================

/*
Console output will look like:

14:23:15.123 💚 D/UserRepository: Fetching user with ID: 123
14:23:15.456 💙 I/UserRepository: User fetched successfully: Alice
14:23:16.789 💛 W/Analytics: Event not tracked
14:23:17.123 ❤️ E/UserRepository: Network error while fetching user: 123
java.net.UnknownHostException: Unable to resolve host
    at ...

Legend:
💜 V - Verbose
💚 D - Debug
💙 I - Info
💛 W - Warning
❤️ E - Error
💞 A - Assert
*/

// ============================================
// 10. Swift Interop Example
// ============================================

/*
From Swift code:

import Timber

class AppDelegate: UIResponder, UIApplicationDelegate {
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        // Initialize Timber
        ExampleKt.initializeTimberForIOS()

        // Use from Swift
        Timber.companion.d("Hello from Swift!")
        Timber.companion.i("User logged in: %s", "Alice")

        return true
    }
}

class ViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()

        Timber.companion.tag("ViewController").d("View loaded")

        do {
            try someOperation()
        } catch {
            Timber.companion.e("Error occurred: \(error)")
        }
    }
}
*/

// ============================================
// Data Classes for Examples
// ============================================

data class User(
    val id: Int,
    val name: String,
    val email: String
)

class NetworkException(message: String) : Exception(message)

// ============================================
// Summary
// ============================================

/*
✅ Key Features Demonstrated:
1. Basic logging (v, d, i, w, e, wtf)
2. Format arguments
3. Exception logging
4. Custom tags
5. Multiple trees
6. Coroutine support
7. Production configuration
8. Swift interop

🎯 Best Practices:
- Always plant trees at app startup
- Use appropriate log levels
- Include context in messages
- Log exceptions with context
- Remove sensitive data in production
- Use custom tags for filtering
- Clean up trees when done

📚 More Information:
- See iOS_USAGE.md for detailed documentation
- See IOS_IMPLEMENTATION_SUMMARY.md for technical details
- Compare with Android usage for consistency
*/
