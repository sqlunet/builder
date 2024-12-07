package org.sqlbuilder.common

abstract class Resolver2<T, U, R>(val map: Map<T, Map<U, R>>) : (T, U) -> R? {

    @Nullable
    override fun invoke(k: T, k2: U): R? {
        return map[k]?.get(k2)
    }
}
