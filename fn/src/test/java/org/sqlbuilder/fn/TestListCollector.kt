package org.sqlbuilder.fn

import org.junit.Test
import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.ListCollector
import org.sqlbuilder.common.SetId

class TestListCollector {
    internal class W<T>(val wrapped: T?) : HasId, SetId {

        private var id = 0

        override fun toString(): String {
            return wrapped.toString()
        }

        override fun getIntId(): Int {
            return id
        }

        override fun setId(id: Int) {
            this.id = id
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
            // System.out.printf("%s%n", item);
            // System.out.printf("%s %d%n", item, collector.indexOf(item) + 1);
            System.out.printf("%s %d%n", item, item.getIntId())
        }
    }
}
