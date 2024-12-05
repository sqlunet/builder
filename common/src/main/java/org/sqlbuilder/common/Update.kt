package org.sqlbuilder.common

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.PrintStream

object Update {

    @Throws(FileNotFoundException::class)
    fun <T : Resolvable<U, R>, U, R> update(
        items: Iterable<T>,
        file: File,
        header: String,
        table: String,
        resolver: (U) -> R?,
        setStringifier: (R?) -> String,
        whereStringifier: (U) -> String,
    ) {
        PrintStream(FileOutputStream(file)).use { ps ->
            ps.println("-- $header")
            if (items.iterator().hasNext()) {
                items.forEach {
                    val resolved = it.resolve(resolver)
                    if (resolved != null) {
                        val resolving = it.resolving()
                        val sqlResolving = whereStringifier.invoke(resolving)
                        val sqlResolved = setStringifier.invoke(resolved)
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
