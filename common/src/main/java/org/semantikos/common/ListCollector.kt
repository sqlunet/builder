package org.semantikos.common

import java.io.Closeable

class ListCollector<T : SetId> : ArrayList<T>(), Closeable {

    private var allocator = 0

    fun open(): ListCollector<T> {
        return this
    }

    override fun add(item: T): Boolean {
        item.setId(++allocator)
        return super.add(item)
    }

    override fun close() {
        clear()
    }

    fun status(): String {
        return ":$size"
    }
}
