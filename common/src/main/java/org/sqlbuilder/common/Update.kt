package org.sqlbuilder.common

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.PrintStream
import java.util.function.Function

object Update {

    @Throws(FileNotFoundException::class)
    @JvmStatic
    fun <T : Resolvable<U, R>, U, R> update(
        items: Iterable<T>,
        file: File,
        header: String,
        table: String,
        resolver: Function<U, R?>,
        setStringifier: Function<R?, String>,
        whereStringifier: Function<U, String>,
    ) {
        PrintStream(FileOutputStream(file)).use { ps ->
            ps.println("-- $header")
            if (items.iterator().hasNext()) {
                items.forEach {
                    val resolved = it.resolve(resolver)
                    if (resolved != null) {
                        val resolving = it.resolving()
                        val sqlResolving = whereStringifier.apply(resolving)
                        val sqlResolved = setStringifier.apply(resolved)
                        val comment = it.comment()
                        var row = "UPDATE $table SET $sqlResolved WHERE $sqlResolving;"
                        if (comment != null) {
                            row = "$row /* $comment */"
                        }
                        ps.println(row)
                    }
                }
            }
        }
    }
}
