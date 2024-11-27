package org.sqlbuilder.common

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.PrintStream
import java.util.function.Consumer
import java.util.function.Function

object Update {

    @Throws(FileNotFoundException::class)
    @JvmStatic
    fun <T : Resolvable<U, R>, U, R> update2(
        items: Iterable<T>,
        file: File,
        header: String,
        table: String,
        resolver: Function<U, R>,
        setStringifier: Function<R, String>,
        whereStringifier: Function<U, String>
    ) {
        PrintStream(FileOutputStream(file)).use { ps ->
            ps.println("-- $header")
            if (items.iterator().hasNext()) {
                items.forEach(Consumer { item: T? ->
                    val resolved = item!!.resolve(resolver)
                    if (resolved != null) {
                        val resolving = item.resolving()
                        val sqlResolving = whereStringifier.apply(resolving)
                        val sqlResolved = setStringifier.apply(resolved)
                        val comment = item.comment()
                        var row = String.format("UPDATE %s SET %s WHERE %s;", table, sqlResolved, sqlResolving)
                        if (comment != null) {
                            row = String.format("%s /* %s */", row, comment)
                        }
                        ps.println(row)
                    }
                })
            }
        }
    }
}
