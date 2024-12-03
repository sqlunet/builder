package org.sqlbuilder2.ser

import java.io.Serializable
import java.util.*

/**
 * Pair
 *
 * @param first  first
 * @param second second
 * @param <T> type of first
 * @param <U> type of second
 */
class Pair<T, U>(
    @JvmField val first: T,
    @JvmField val second: U,
) : Serializable {

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as Pair<*, *>
        return first == that.first && second == that.second
    }

    override fun hashCode(): Int {
        return Objects.hash(first, second)
    }

    override fun toString(): String {
        return "($first,$second)"
    }
}
