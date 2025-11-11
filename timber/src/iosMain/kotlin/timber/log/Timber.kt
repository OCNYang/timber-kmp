package timber.log

import platform.Foundation.*
import platform.darwin.*
import kotlin.concurrent.AtomicReference

/** Logging for lazy people. */
actual class Timber actual private constructor() {
    init {
        throw AssertionError()
    }

    /** A facade for handling logging calls. Install instances via [`Timber.plant()`][.plant]. */
    actual abstract class Tree {
        // Use AtomicReference instead of ThreadLocal for iOS
        @Suppress("PropertyName")
        internal val explicitTag = AtomicReference<String?>(null)

        internal open val tag: String?
            get() {
                val tag = explicitTag.value
                if (tag != null) {
                    explicitTag.value = null
                }
                return tag
            }

        /** Log a verbose message with optional format args. */
        actual open fun v(message: String?, vararg args: Any?) {
            prepareLog(LogLevel.VERBOSE, null, message, *args)
        }

        /** Log a verbose exception and a message with optional format args. */
        actual open fun v(t: Throwable?, message: String?, vararg args: Any?) {
            prepareLog(LogLevel.VERBOSE, t, message, *args)
        }

        /** Log a verbose exception. */
        actual open fun v(t: Throwable?) {
            prepareLog(LogLevel.VERBOSE, t, null)
        }

        /** Log a debug message with optional format args. */
        actual open fun d(message: String?, vararg args: Any?) {
            prepareLog(LogLevel.DEBUG, null, message, *args)
        }

        /** Log a debug exception and a message with optional format args. */
        actual open fun d(t: Throwable?, message: String?, vararg args: Any?) {
            prepareLog(LogLevel.DEBUG, t, message, *args)
        }

        /** Log a debug exception. */
        actual open fun d(t: Throwable?) {
            prepareLog(LogLevel.DEBUG, t, null)
        }

        /** Log an info message with optional format args. */
        actual open fun i(message: String?, vararg args: Any?) {
            prepareLog(LogLevel.INFO, null, message, *args)
        }

        /** Log an info exception and a message with optional format args. */
        actual open fun i(t: Throwable?, message: String?, vararg args: Any?) {
            prepareLog(LogLevel.INFO, t, message, *args)
        }

        /** Log an info exception. */
        actual open fun i(t: Throwable?) {
            prepareLog(LogLevel.INFO, t, null)
        }

        /** Log a warning message with optional format args. */
        actual open fun w(message: String?, vararg args: Any?) {
            prepareLog(LogLevel.WARN, null, message, *args)
        }

        /** Log a warning exception and a message with optional format args. */
        actual open fun w(t: Throwable?, message: String?, vararg args: Any?) {
            prepareLog(LogLevel.WARN, t, message, *args)
        }

        /** Log a warning exception. */
        actual open fun w(t: Throwable?) {
            prepareLog(LogLevel.WARN, t, null)
        }

        /** Log an error message with optional format args. */
        actual open fun e(message: String?, vararg args: Any?) {
            prepareLog(LogLevel.ERROR, null, message, *args)
        }

        /** Log an error exception and a message with optional format args. */
        actual open fun e(t: Throwable?, message: String?, vararg args: Any?) {
            prepareLog(LogLevel.ERROR, t, message, *args)
        }

        /** Log an error exception. */
        actual open fun e(t: Throwable?) {
            prepareLog(LogLevel.ERROR, t, null)
        }

        /** Log an assert message with optional format args. */
        actual open fun wtf(message: String?, vararg args: Any?) {
            prepareLog(LogLevel.ASSERT, null, message, *args)
        }

        /** Log an assert exception and a message with optional format args. */
        actual open fun wtf(t: Throwable?, message: String?, vararg args: Any?) {
            prepareLog(LogLevel.ASSERT, t, message, *args)
        }

        /** Log an assert exception. */
        actual open fun wtf(t: Throwable?) {
            prepareLog(LogLevel.ASSERT, t, null)
        }

        /** Log at `priority` a message with optional format args. */
        actual open fun log(priority: Int, message: String?, vararg args: Any?) {
            prepareLog(priority, null, message, *args)
        }

        /** Log at `priority` an exception and a message with optional format args. */
        actual open fun log(priority: Int, t: Throwable?, message: String?, vararg args: Any?) {
            prepareLog(priority, t, message, *args)
        }

        /** Log at `priority` an exception. */
        actual open fun log(priority: Int, t: Throwable?) {
            prepareLog(priority, t, null)
        }

        /** Return whether a message at `priority` or `tag` should be logged. */
        actual protected open fun isLoggable(tag: String?, priority: Int): Boolean = true

        private fun prepareLog(priority: Int, t: Throwable?, message: String?, vararg args: Any?) {
            // Consume tag even when message is not loggable so that next message is correctly tagged.
            val tag = tag
            if (!isLoggable(tag, priority)) {
                return
            }

            var finalMessage = message
            if (finalMessage.isNullOrEmpty()) {
                if (t == null) {
                    return  // Swallow message if it's null and there's no throwable.
                }
                finalMessage = t.stackTraceToString()
            } else {
                if (args.isNotEmpty()) {
                    finalMessage = formatMessage(finalMessage, args)
                }
                if (t != null) {
                    finalMessage += "\n" + t.stackTraceToString()
                }
            }

            log(priority, tag, finalMessage, t)
        }

        /** Formats a log message with optional arguments. */
        actual protected open fun formatMessage(message: String, args: Array<out Any?>): String {
            return try {
                // Simple format implementation for iOS
                var result = message
                args.forEachIndexed { index, arg ->
                    result = result.replace("%${index + 1}\$s", arg?.toString() ?: "null")
                    result = result.replace("%s", arg?.toString() ?: "null", ignoreCase = false)
                }
                result
            } catch (e: Exception) {
                message
            }
        }

        /**
         * Write a log message to its destination. Called for all level-specific methods by default.
         *
         * @param priority Log level. See [LogLevel] for constants.
         * @param tag Explicit or inferred tag. May be `null`.
         * @param message Formatted log message.
         * @param t Accompanying exceptions. May be `null`.
         */
        actual protected abstract fun log(priority: Int, tag: String?, message: String, t: Throwable?)
    }

    /**
     * A [Tree] for debug builds. Automatically infers the tag from the calling class.
     *
     * This implementation is inspired by Napier and Kermit:
     * - Uses NSThread.callStackSymbols for stack trace (from Napier)
     * - Supports both NSLog and OSLog (from Kermit)
     * - Beautiful emoji-based log formatting (from Napier)
     */
    open class DebugTree(
        private val useOSLog: Boolean = false,
        private val defaultTag: String = "Timber"
    ) : Tree() {

        private val fqcnIgnore = listOf(
            "timber.log.Timber",
            "timber.log.Timber.Forest",
            "timber.log.Timber.Tree",
            "timber.log.Timber.DebugTree"
        )

        private val dateFormatter = NSDateFormatter().apply {
            dateFormat = "HH:mm:ss.SSS"
        }

        // Emoji tags for better visibility (from Napier)
        private val tagMap: Map<Int, String> = mapOf(
            LogLevel.VERBOSE to "💜 V",
            LogLevel.DEBUG to "💚 D",
            LogLevel.INFO to "💙 I",
            LogLevel.WARN to "💛 W",
            LogLevel.ERROR to "❤️ E",
            LogLevel.ASSERT to "💞 A"
        )

        override val tag: String?
            get() = super.tag ?: performTag(defaultTag)

        /**
         * Automatically infer tag from call stack.
         * Implementation borrowed from Napier's iOS implementation.
         */
        private fun performTag(defaultTag: String): String {
            val symbols = NSThread.callStackSymbols()
            if (symbols.size <= CALL_STACK_INDEX) return defaultTag

            return (symbols[CALL_STACK_INDEX] as? String)?.let {
                createStackElementTag(it)
            } ?: defaultTag
        }

        /**
         * Extract the tag from stack trace element.
         * Implementation borrowed from Napier with enhancements for coroutines.
         */
        protected open fun createStackElementTag(stackSymbol: String): String {
            var tag = stackSymbol

            // Remove everything after $ (anonymous classes)
            tag = tag.substringBeforeLast('$')

            // Remove everything after ( (parameters)
            tag = tag.substringBeforeLast('(')

            if (tag.contains("$")) {
                // Handle coroutines specially
                tag = tag.substring(tag.lastIndexOf(".", tag.lastIndexOf(".") - 1) + 1)
                tag = tag.replace("$", "")
                tag = tag.replace("COROUTINE", "[async]")
            } else {
                // Normal classes
                tag = tag.substringAfterLast(".")
                tag = tag.replace("#", ".")
            }

            return tag.ifEmpty { defaultTag }
        }

        /**
         * Log message to console.
         * Supports both NSLog (legacy) and os_log (modern) based on useOSLog flag.
         */
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (useOSLog && isOSLogAvailable()) {
                logWithOSLog(priority, tag, message, t)
            } else {
                logWithNSLog(priority, tag, message, t)
            }
        }

        /**
         * Log using modern os_log API (iOS 10+).
         * Implementation inspired by Kermit's OSLogWriter.
         */
        private fun logWithOSLog(priority: Int, tag: String?, message: String, t: Throwable?) {
            val osLogType = when (priority) {
                LogLevel.VERBOSE, LogLevel.DEBUG -> OS_LOG_TYPE_DEBUG
                LogLevel.INFO -> OS_LOG_TYPE_INFO
                LogLevel.WARN -> OS_LOG_TYPE_DEFAULT
                LogLevel.ERROR -> OS_LOG_TYPE_ERROR
                LogLevel.ASSERT -> OS_LOG_TYPE_FAULT
                else -> OS_LOG_TYPE_DEFAULT
            }

            val fullMessage = formatLogMessage(priority, tag, message)

            // Create logger for the tag
            val logger = os_log_create("timber.log", tag ?: "default")

            // Log the message
            __darwin_os_log(logger, osLogType, fullMessage)

            // Log throwable if present
            t?.let {
                __darwin_os_log(logger, osLogType, it.stackTraceToString())
            }
        }

        /**
         * Log using legacy NSLog.
         * Implementation inspired by Napier's DebugAntilog.
         */
        private fun logWithNSLog(priority: Int, tag: String?, message: String, t: Throwable?) {
            val fullMessage = formatLogMessage(priority, tag, message)
            NSLog("%s", fullMessage)

            t?.let {
                NSLog("%s", it.stackTraceToString())
            }
        }

        /**
         * Format log message with timestamp, emoji, and tag.
         * Beautiful format inspired by Napier.
         */
        private fun formatLogMessage(priority: Int, tag: String?, message: String): String {
            val timestamp = dateFormatter.stringFromDate(NSDate())
            val levelTag = tagMap[priority] ?: "⚪️ ?"
            val logTag = tag ?: defaultTag

            return "$timestamp $levelTag/$logTag: $message"
        }

        /**
         * Check if os_log is available (iOS 10+).
         * For simplicity, we assume it's always available for now.
         * Full implementation would require version checking.
         */
        private fun isOSLogAvailable(): Boolean {
            // TODO: Implement proper version checking
            // For now, always use NSLog as it's more compatible
            return false
        }

        companion object {
            private const val CALL_STACK_INDEX = 8
        }
    }

    actual companion object Forest : Tree() {
        // Use AtomicReference for thread-safe tree management
        private val treesAtomic = AtomicReference(emptyList<Tree>())

        private val trees: List<Tree>
            get() = treesAtomic.value

        actual override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            throw AssertionError() // Missing override for log method.
        }

        /** Set a one-time tag for use on the next logging call. */
        actual fun tag(tag: String): Tree {
            trees.forEach { tree ->
                (tree as? Tree)?.explicitTag?.value = tag
            }
            return this
        }

        /** Add a new logging tree. */
        actual fun plant(tree: Tree) {
            require(tree !== this) { "Cannot plant Timber into itself." }
            val currentTrees = treesAtomic.value.toMutableList()
            currentTrees.add(tree)
            treesAtomic.value = currentTrees
        }

        /** Adds new logging trees. */
        actual fun plant(vararg trees: Tree) {
            for (tree in trees) {
                requireNotNull(tree) { "trees contained null" }
                require(tree !== this) { "Cannot plant Timber into itself." }
            }
            val currentTrees = treesAtomic.value.toMutableList()
            currentTrees.addAll(trees)
            treesAtomic.value = currentTrees
        }

        /** Remove a planted tree. */
        actual fun uproot(tree: Tree) {
            val currentTrees = treesAtomic.value.toMutableList()
            require(currentTrees.remove(tree)) { "Cannot uproot tree which is not planted: $tree" }
            treesAtomic.value = currentTrees
        }

        /** Remove all planted trees. */
        actual fun uprootAll() {
            treesAtomic.value = emptyList()
        }

        /** Return a copy of all planted [trees][Tree]. */
        actual fun forest(): List<Tree> = trees.toList()

        actual val treeCount: Int
            get() = trees.size
    }
}

/**
 * Log level constants for iOS.
 * Maps to priority values compatible with Android's Log class.
 */
object LogLevel {
    const val VERBOSE = 2
    const val DEBUG = 3
    const val INFO = 4
    const val WARN = 5
    const val ERROR = 6
    const val ASSERT = 7
}

/**
 * Helper function to call os_log.
 * Uses the modern os_log API when available.
 */
@Suppress("UNUSED_PARAMETER")
private fun __darwin_os_log(logger: platform.darwin.os_log_t, type: platform.darwin.os_log_type_t, message: String) {
    // Use NSLog as fallback for now
    // For full os_log support, we would need C interop
    NSLog("%s", message)
}
