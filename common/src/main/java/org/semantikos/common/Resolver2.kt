package org.semantikos.common

abstract class Resolver2<T, U, R>(val map: Map<T, Map<U, R>>) : (T, U) -> R? {

    override fun invoke(k: T, k2: U): R? {
        return map[k]?.get(k2)
    }
}
