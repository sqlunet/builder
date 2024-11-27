package org.sqlbuilder.common

object Progress {

    @JvmStatic
    fun tracePending(tag: String, message: String?) {
        print("$tag $message")
    }

    @JvmStatic
    @JvmOverloads
    fun traceDone(message: String? = null) {
        if (message == null) {
            println(" ✓")
        } else {
            System.err.println(" ✘ $message")
        }
    }

    @JvmStatic
    fun traceHeader(tag: String, message: String) {
        System.err.println(">$tag $message")
    }

    @JvmStatic
    fun traceTailer(tag: String, message: String) {
        System.err.println("<$tag $message")
    }

    @JvmStatic
    fun traceTailer(count: Long) {
        System.err.println("<$count")
    }

    @JvmStatic
    fun trace(tag: String, message: String?) {
        System.err.println("$tag $message")
    }

    @JvmStatic
    fun trace(message: String) {
        System.err.println("mesg: $message")
    }

    private const val GRANULARITY: Long = 10

    private const val PERLINE: Long = 100

    @JvmStatic
    fun trace(count: Long) {
        if (count % GRANULARITY == 0L) {
            System.err.print('.')
        }
        if (count % (GRANULARITY * PERLINE) == 0L) {
            System.err.print('\n')
        }
    }

    @JvmStatic
    fun info(message: String) {
        System.err.println(message)
    }
}
