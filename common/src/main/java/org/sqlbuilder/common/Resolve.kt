package org.sqlbuilder.common

interface Resolve<T, R> {

    fun resolve(what: T): R
}