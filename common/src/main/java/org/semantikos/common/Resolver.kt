package org.semantikos.common

abstract class Resolver<T, R>(val map: Map<T, R>) : (T) -> R? {

    override fun invoke(k: T): R? {
        return map[k]
    }
}
