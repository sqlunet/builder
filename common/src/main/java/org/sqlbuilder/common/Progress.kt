package org.sqlbuilder.common

object Progress {

    fun tracePending(tag: String, message: String?) {
        print("$tag $message")
    }

    @JvmOverloads
    fun traceDone(message: String? = null) {
        if (message == null) {
            println(" ✓")
        } else {
            System.err.println(" ✘ $message")
        }
    }

    fun traceHeader(tag: String, message: String) {
        System.err.println(">$tag $message")
    }

    fun traceTailer(tag: String, message: String) {
        System.err.println("<$tag $message")
    }

    fun traceTailer(count: Long) {
        System.err.println("<$count")
    }

    fun trace(tag: String, message: String?) {
        System.err.println("$tag $message")
    }

    fun trace(message: String) {
        System.err.println("mesg: $message")
    }

    private const val GRANULARITY: Long = 10

    private const val PERLINE: Long = 100

    fun trace(count: Long) {
        if (count % GRANULARITY == 0L) {
            System.err.print('.')
        }
        if (count % (GRANULARITY * PERLINE) == 0L) {
            System.err.print('\n')
        }
    }

    fun info(message: String) {
        System.err.println(message)
    }
}
