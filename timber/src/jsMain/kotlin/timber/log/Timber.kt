package timber.log

/** Logging for lazy people. */
actual class Timber actual private constructor() {
    init {
        throw AssertionError()
    }

    /** A facade for handling logging calls. Install instances via [`Timber.plant()`][.plant]. */
    actual abstract class Tree {
        // JS is single-threaded, use simple property
        internal var explicitTag: String? = null

        internal open val tag: String?
            get() {
                val tag = explicitTag
                if (tag != null) {
                    explicitTag = null
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
            // JS doesn't have String.format, so just replace %s with values
            var result = message
            for (arg in args) {
                result = result.replaceFirst("%s", arg?.toString() ?: "null")
            }
            return result
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
     * A [Tree] for debug builds. Outputs to browser/Node.js console.
     *
     * This implementation is inspired by Kermit and Napier:
     * - Uses console interface abstraction (from Kermit)
     * - Simple, clean implementation (from Napier)
     */
    open class DebugTree(
        private val defaultTag: String = "Timber"
    ) : Tree() {

        override val tag: String?
            get() = super.tag ?: defaultTag

        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            val fullMessage = formatLogMessage(priority, tag, message)

            when (priority) {
                LogLevel.VERBOSE, LogLevel.DEBUG -> console.log(fullMessage)
                LogLevel.INFO -> console.info(fullMessage)
                LogLevel.WARN -> console.warn(fullMessage)
                LogLevel.ERROR, LogLevel.ASSERT -> console.error(fullMessage)
                else -> console.log(fullMessage)
            }

            // Log throwable separately
            t?.let {
                console.error(it.stackTraceToString())
            }
        }

        private fun formatLogMessage(priority: Int, tag: String?, message: String): String {
            val levelName = when (priority) {
                LogLevel.VERBOSE -> "VERBOSE"
                LogLevel.DEBUG -> "DEBUG"
                LogLevel.INFO -> "INFO"
                LogLevel.WARN -> "WARN"
                LogLevel.ERROR -> "ERROR"
                LogLevel.ASSERT -> "ASSERT"
                else -> "UNKNOWN"
            }

            return "[$levelName] ${tag ?: defaultTag}: $message"
        }
    }

    actual companion object Forest : Tree() {
        private val trees = mutableListOf<Tree>()

        actual override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            throw AssertionError() // Missing override for log method.
        }

        /** Set a one-time tag for use on the next logging call. */
        actual fun tag(tag: String): Tree {
            trees.forEach { tree ->
                tree.explicitTag = tag
            }
            return this
        }

        /** Add a new logging tree. */
        actual fun plant(tree: Tree) {
            require(tree !== this) { "Cannot plant Timber into itself." }
            trees.add(tree)
        }

        /** Adds new logging trees. */
        actual fun plant(vararg trees: Tree) {
            for (tree in trees) {
                requireNotNull(tree) { "trees contained null" }
                require(tree !== this) { "Cannot plant Timber into itself." }
            }
            this.trees.addAll(trees)
        }

        /** Remove a planted tree. */
        actual fun uproot(tree: Tree) {
            require(trees.remove(tree)) { "Cannot uproot tree which is not planted: $tree" }
        }

        /** Remove all planted trees. */
        actual fun uprootAll() {
            trees.clear()
        }

        /** Return a copy of all planted [trees][Tree]. */
        actual fun forest(): List<Tree> = trees.toList()

        actual val treeCount: Int
            get() = trees.size
    }
}

/**
 * Log level constants for JS.
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
