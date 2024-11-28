package org.sqlbuilder.common

import java.util.function.Function

abstract class Resolver<T, R>(val map: Map<T, R>) : Function<T, R> {

    @Nullable
    override fun apply(k: T): R {
        return map[k]!!
    }
}
