package org.semantikos.fn

import org.junit.Test
import org.semantikos.common.HasId
import org.semantikos.common.ListCollector
import org.semantikos.common.SetId

class TestListCollector {
    internal class W<T>(val wrapped: T?) : HasId, SetId {

        private var id = 0

        override fun toString(): String {
            return wrapped.toString()
        }

        override fun getIntId(): Int {
            return id
        }

        override fun setId(id0: Int) {
            id = id0
        }

        companion object {

            fun <T> w(wrapped: T?): W<T?> {
                return W<T?>(wrapped)
            }
        }
    }

    @Test
    fun testListCollector() {
        val collector = ListCollector<W<String?>>()
        collector.add(W.Companion.w<String?>("one"))
        collector.add(W.Companion.w<String?>("two"))
        collector.add(W.Companion.w<String?>("two"))
        collector.add(W.Companion.w<String?>("one"))
        collector.add(W.Companion.w<String?>("three"))
        for (item in collector) {
            // println(item)
            // println("$item ${collector.indexOf(item) + 1}")
            println("$item ${item.intId}")
        }
    }
}
