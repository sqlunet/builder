package org.sqlbuilder.fn.joins

import java.util.*

open class Triple<T, U, V>(
    val first: T,
    val second: U, val third: V
) {

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as Triple<*, *, *>
        return first == that.first && second == that.second && third == that.third
    }

    override fun hashCode(): Int {
        return Objects.hash(first, second, third)
    }

    override fun toString(): String {
        return String.format("[first=%s second=%s third=%s]", first, second, third)
    }
}
