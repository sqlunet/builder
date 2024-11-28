package org.sqlbuilder.common

import java.util.function.BiFunction

abstract class Resolver2<T, U, R>(val map: Map<T, Map<U, R>>) : BiFunction<T, U, R> {

    @Nullable
    override fun apply(k: T, k2: U): R {
        val m2 = map[k]!!
        return m2[k2]!!
    }
}
