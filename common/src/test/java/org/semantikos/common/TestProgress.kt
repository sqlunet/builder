package org.semantikos.common

import org.junit.Test

class TestProgress {

    fun displayProgress(progress: Int, total: Int) {
        if (progress % GRANULARITY == 0) {
            var occurs: Int = progress / GRANULARITY
            val c = when (occurs % 4) {
                0    -> '—'
                1    -> '\\'
                2    -> '|'
                3    -> '/'
                else -> '.'
            }
            print("\r$c $progress")
        }
    }

    fun displayProgressDots(i: Int, total: Int) {
        Progress.trace(i.toLong())
    }

    fun displayProgressBarPercent(i: Int, total: Int) {
        val progress = ((i * 100.0) / total).toInt()
        val p = "=".repeat(progress / scale)
        val r = ".".repeat(50 - progress / scale)
        print("\rProgress: [$p>$r] $progress%")
    }

    fun displayDone(total: Int) {
        //println("\r!")
        println("\r$total")
    }

    @Test
    fun testProgressBar() {
        for (i in 1..total) {
            displayProgress(i, total)
            try {
                Thread.sleep(interval)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        displayDone(total)
    }

    companion object {

        // ⸺ ⸏ ― — —
        private const val GRANULARITY = 10

        const val total = 666

        const val scale = 2

        const val interval = 50L

        @JvmStatic
        fun main(args: Array<String>) {
            TestProgress().testProgressBar()
        }
    }
}
