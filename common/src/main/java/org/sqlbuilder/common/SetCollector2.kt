package org.sqlbuilder.common

import java.io.Closeable
import java.util.*

class SetCollector2<T>(comparator: Comparator<T>) : Resolve<T, Int> , Closeable {

    val map = TreeMap<T, Int?>(comparator)

    private var isOpen = false

    fun open(): SetCollector2<T> {
        var i = 1
        for (k in map.keys) {
            map.put(k, i++)
        }
        isOpen = true
        return this
    }

    /**
     * Add item key to map
     *
     * @param item item key
     * @return false if already there
     */
    fun add(item: T): Boolean {
        // avoid changing value to null
        // putIfAbsent(item, null) uses get and throw not-open exception
        if (map.containsKey(item)) {
            return false
        }
        return map.put(item, null) == null // null if there was no mapping
    }

    override fun resolve(key: T): Int {
        check(isOpen) { "$this not open" }
        return map.get(key)!!
    }

    override fun close() {
        isOpen = false
        map.clear()
    }

    fun status(): String {
        return "#${map.size}"
    }
}
