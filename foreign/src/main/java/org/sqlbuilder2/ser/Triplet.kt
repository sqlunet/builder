package org.sqlbuilder2.ser

import java.io.Serializable
import java.util.*

/**
 * Triplet
 *
 * @param first  first
 * @param second second
 * @param third  third
 * @param <T> type of first
 * @param <U> type of second
 * @param <V> type of third
 */
class Triplet<T, U, V>(
    @JvmField val first: T,
    @JvmField val second: U,
    @JvmField val third: V,
) : Serializable {

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as Triplet<*, *, *>
        return first == that.first && second == that.second && third == that.third
    }

    override fun hashCode(): Int {
        return Objects.hash(first, second, third)
    }

    override fun toString(): String {
        return "($first,$second,$third)"
    }
}
