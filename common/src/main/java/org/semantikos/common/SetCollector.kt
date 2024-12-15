package org.semantikos.common

import java.io.Closeable
import java.util.*

class SetCollector<T>(comparator: Comparator<T>) : Iterable<T>, (T) -> Int, Closeable {

    private val map = TreeMap<T, Int?>(comparator)

    private var isOpen = false

    fun open(): SetCollector<T> {
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

    val size
        get() = map.size

    override fun iterator(): Iterator<T> {
        return map.keys.iterator()
    }

    override fun invoke(key: T): Int {
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

    fun toMap(toString: (T) -> String): Map<String, Int> {
        return this
            .asSequence()
            .map { toString(it) to invoke(it) }
            .toMap()
            .toSortedMap()
    }
}
