package timber.log

import java.io.PrintWriter
import java.io.StringWriter
import java.util.ArrayList
import java.util.Collections
import java.util.logging.*
import java.util.regex.Pattern

/** Logging for lazy people. */
actual class Timber actual private constructor() {
    init {
        throw AssertionError()
    }

    /** A facade for handling logging calls. Install instances via [`Timber.plant()`][.plant]. */
    actual abstract class Tree {
        @get:JvmSynthetic // Hide from public API.
        internal val explicitTag = ThreadLocal<String>()

        @get:JvmSynthetic // Hide from public API.
        internal open val tag: String?
            get() {
                val tag = explicitTag.get()
                if (tag != null) {
                    explicitTag.remove()
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
                finalMessage = getStackTraceString(t)
            } else {
                if (args.isNotEmpty()) {
                    finalMessage = formatMessage(finalMessage, args)
                }
                if (t != null) {
                    finalMessage += "\n" + getStackTraceString(t)
                }
            }

            log(priority, tag, finalMessage, t)
        }

        /** Formats a log message with optional arguments. */
        actual protected open fun formatMessage(message: String, args: Array<out Any?>): String {
            return message.format(*args)
        }

        protected fun getStackTraceString(t: Throwable): String {
            // Don't replace this with Log.getStackTraceString() - it hides
            // UnknownHostException, which is not what we want.
            val sw = StringWriter(256)
            val pw = PrintWriter(sw, false)
            t.printStackTrace(pw)
            pw.flush()
            return sw.toString()
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
     * - Uses Thread.stackTrace for automatic TAG (from Napier)
     * - Supports both System.out and java.util.logging (from Kermit)
     */
    open class DebugTree(
        private val useJavaLogging: Boolean = false,
        private val defaultTag: String = "Timber"
    ) : Tree() {

        private val fqcnIgnore = listOf(
            Timber::class.java.name,
            Forest::class.java.name,
            Tree::class.java.name,
            DebugTree::class.java.name
        )

        private val logger: Logger by lazy {
            Logger.getLogger(DebugTree::class.java.name).apply {
                level = Level.ALL
                useParentHandlers = false
                addHandler(ConsoleHandler().apply {
                    level = Level.ALL
                    formatter = SimpleFormatter()
                })
            }
        }

        override val tag: String?
            get() = super.tag ?: performTag(defaultTag)

        /**
         * Automatically infer tag from call stack.
         * Implementation borrowed from Napier's JVM implementation.
         */
        private fun performTag(defaultTag: String): String {
            val thread = Thread.currentThread().stackTrace

            return if (thread.size >= CALL_STACK_INDEX) {
                thread[CALL_STACK_INDEX].run {
                    createStackElementTag(className)
                }
            } else {
                defaultTag
            }
        }

        /**
         * Extract the tag from class name.
         * Implementation borrowed from Napier with enhancements.
         */
        protected open fun createStackElementTag(className: String): String {
            var tag = className
            val m = ANONYMOUS_CLASS.matcher(tag)
            if (m.find()) {
                tag = m.replaceAll("")
            }
            return tag.substring(tag.lastIndexOf('.') + 1)
        }

        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (useJavaLogging) {
                logWithJavaLogging(priority, tag, message, t)
            } else {
                logWithSystemOut(priority, tag, message, t)
            }
        }

        /**
         * Log using java.util.logging.
         * Implementation inspired by Napier.
         */
        private fun logWithJavaLogging(priority: Int, tag: String?, message: String, t: Throwable?) {
            val level = when (priority) {
                LogLevel.VERBOSE -> Level.FINEST
                LogLevel.DEBUG -> Level.FINE
                LogLevel.INFO -> Level.INFO
                LogLevel.WARN -> Level.WARNING
                LogLevel.ERROR, LogLevel.ASSERT -> Level.SEVERE
                else -> Level.INFO
            }

            val fullMessage = formatLogMessage(priority, tag, message)

            if (t != null) {
                logger.log(level, fullMessage, t)
            } else {
                logger.log(level, fullMessage)
            }
        }

        /**
         * Log using System.out/err.
         * Implementation inspired by Kermit.
         */
        private fun logWithSystemOut(priority: Int, tag: String?, message: String, t: Throwable?) {
            val fullMessage = formatLogMessage(priority, tag, message)

            if (priority >= LogLevel.ERROR) {
                System.err.println(fullMessage)
                t?.let { System.err.println(getStackTraceString(it)) }
            } else {
                println(fullMessage)
                t?.let { println(getStackTraceString(it)) }
            }
        }

        private fun formatLogMessage(priority: Int, tag: String?, message: String): String {
            val levelName = when (priority) {
                LogLevel.VERBOSE -> "V"
                LogLevel.DEBUG -> "D"
                LogLevel.INFO -> "I"
                LogLevel.WARN -> "W"
                LogLevel.ERROR -> "E"
                LogLevel.ASSERT -> "A"
                else -> "?"
            }

            val timestamp = java.time.LocalTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
            )

            return "$timestamp [$levelName] ${tag ?: defaultTag}: $message"
        }

        companion object {
            private const val CALL_STACK_INDEX = 8
            private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")
        }
    }

    actual companion object Forest : Tree() {
        // Both fields guarded by 'trees'.
        private val trees = ArrayList<Tree>()
        @Volatile
        private var treeArray = emptyArray<Tree>()

        actual override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            throw AssertionError() // Missing override for log method.
        }

        /** Set a one-time tag for use on the next logging call. */
        @JvmStatic
        actual fun tag(tag: String): Tree {
            for (tree in treeArray) {
                tree.explicitTag.set(tag)
            }
            return this
        }

        /** Add a new logging tree. */
        @JvmStatic
        actual fun plant(tree: Tree) {
            require(tree !== this) { "Cannot plant Timber into itself." }
            synchronized(trees) {
                trees.add(tree)
                treeArray = trees.toTypedArray()
            }
        }

        /** Adds new logging trees. */
        @JvmStatic
        actual fun plant(vararg trees: Tree) {
            for (tree in trees) {
                requireNotNull(tree) { "trees contained null" }
                require(tree !== this) { "Cannot plant Timber into itself." }
            }
            synchronized(this.trees) {
                Collections.addAll(this.trees, *trees)
                treeArray = this.trees.toTypedArray()
            }
        }

        /** Remove a planted tree. */
        @JvmStatic
        actual fun uproot(tree: Tree) {
            synchronized(trees) {
                require(trees.remove(tree)) { "Cannot uproot tree which is not planted: $tree" }
                treeArray = trees.toTypedArray()
            }
        }

        /** Remove all planted trees. */
        @JvmStatic
        actual fun uprootAll() {
            synchronized(trees) {
                trees.clear()
                treeArray = emptyArray()
            }
        }

        /** Return a copy of all planted [trees][Tree]. */
        @JvmStatic
        actual fun forest(): List<Tree> {
            synchronized(trees) {
                return Collections.unmodifiableList(trees.toList())
            }
        }

        @get:[JvmStatic JvmName("treeCount")]
        actual val treeCount: Int
            get() = treeArray.size
    }
}

/**
 * Log level constants for JVM.
 * Compatible with Android's Log class.
 */
object LogLevel {
    const val VERBOSE = 2
    const val DEBUG = 3
    const val INFO = 4
    const val WARN = 5
    const val ERROR = 6
    const val ASSERT = 7
}
