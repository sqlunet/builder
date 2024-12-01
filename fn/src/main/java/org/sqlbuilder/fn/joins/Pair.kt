package org.sqlbuilder.fn.joins

import java.util.*

open class Pair<T, U>(
    val first: T,
    val second: U) {

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
        return String.format("[first=$first second=$second]")
    }
}
