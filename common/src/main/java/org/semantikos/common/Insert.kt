package org.semantikos.common

import org.semantikos.common.Utils.escape
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.PrintStream
import kotlin.Throws

object Insert {

    // I N S E R T A B L E S

    @Throws(FileNotFoundException::class)
    fun <T : Insertable> insert(
        items: Iterable<T>,
        file: File,
        table: String,
        columns: String,
        header: String,
    ) {
        PrintStream(FileOutputStream(file)).use { ps ->
            ps.println("-- $header")
            if (items.iterator().hasNext()) {
                ps.println("INSERT INTO $table ($columns) VALUES")
                var first = true
                items.forEach {
                    if (first) {
                        first = false
                    } else {
                        ps.println(",")
                    }
                    val values = it.dataRow()
                    val comment = it.comment()
                    val row = if (comment != null) "($values) /* $comment */" else "($values)"
                    ps.print(row)
                }
                ps.println(";")
            }
        }
    }

    @Throws(FileNotFoundException::class)
    fun <T : Insertable> insert(
        items: Iterable<T>,
        comparator: Comparator<T>?,
        file: File,
        table: String,
        columns: String,
        header: String,
    ) {
        PrintStream(FileOutputStream(file)).use { ps ->
            ps.println("-- $header")
            if (items.iterator().hasNext()) {
                ps.println("INSERT INTO $table ($columns) VALUES")
                var seq = items.asSequence()
                if (comparator != null) {
                    seq = seq.sortedWith(comparator)
                }
                var first = true
                seq.forEach {
                    if (first) {
                        first = false
                    } else {
                        ps.println(",")
                    }
                    val values = it.dataRow()
                    val comment = it.comment()
                    val row = if (comment != null) "($values) /* $comment */" else "($values)"
                    ps.print(row)
                }
                ps.println(";")
            }
        }
    }

    @Throws(FileNotFoundException::class)
    fun <T : Insertable> insert(
        items: Iterable<T>,
        resolver: (T) -> Int,
        file: File,
        table: String,
        columns: String,
        header: String,
    ) {
        insert<T>(items, resolver, file, table, columns, header, true)
    }

    @Throws(FileNotFoundException::class)
    fun <T : Insertable> insert(
        items: Iterable<T>,
        resolver: (T) -> Int,
        file: File,
        table: String,
        columns: String,
        header: String,
        withNumber: Boolean = true,
    ) {
        PrintStream(FileOutputStream(file)).use { ps ->
            ps.println("-- $header")
            if (items.iterator().hasNext()) {
                ps.println("INSERT INTO $table ($columns) VALUES")
                var first = true
                items.forEach {
                    if (first) {
                        first = false
                    } else {
                        ps.println(",")
                    }
                    val id = resolver.invoke(it)
                    val values = it.dataRow()
                    val comment = it.comment()
                    val row =
                        if (withNumber)
                            (if (comment != null) "($id,$values) /* $comment */" else "($id,$values)")
                        else
                            (if (comment != null) "($values) /* $comment */" else "($values)")
                    ps.print(row)
                }
                ps.println(";")
            }
        }
    }

    @Throws(FileNotFoundException::class)
    fun <T : Insertable> insertAndIncrement(
        items: Iterable<T>,
        comparator: Comparator<T>?,
        file: File,
        table: String,
        columns: String,
        header: String,
    ) {
        PrintStream(FileOutputStream(file)).use { ps ->
            ps.println("-- $header")
            if (items.iterator().hasNext()) {
                ps.println("INSERT INTO $table ($columns) VALUES")
                var seq = items.asSequence()
                if (comparator != null) {
                    seq = seq.sortedWith(comparator)
                }
                var i = 0
                seq.forEach {
                    if (i > 0) {
                        ps.println(",")
                    }
                    val values = it.dataRow()
                    val comment = it.comment()
                    val row = if (comment != null) "(${i + 1},$values) /* $comment */" else "(${i + 1},$values)"
                    ps.print(row)
                    i++
                }
                ps.println(";")
            }
        }
    }

    @Throws(FileNotFoundException::class)
    fun <T : Insertable> insertFragmented(
        items: Iterable<T>,
        comparator: Comparator<T>?,
        file: File,
        table: String,
        columns: String,
        header: String,
    ) {
        PrintStream(FileOutputStream(file)).use { ps ->
            ps.println("-- $header")
            if (items.iterator().hasNext()) {
                ps.println("INSERT INTO $table ($columns) VALUES")
                var seq = items.asSequence()
                if (comparator != null) {
                    seq = seq.sortedWith(comparator)
                }
                var i = 0
                seq.forEach {
                    if (i == 100000) {
                        ps.println(";")
                        ps.println("INSERT INTO $table ($columns) VALUES")
                        i = 0
                    }
                    if (i > 0) {
                        ps.println(",")
                    }
                    val values = it.dataRow()
                    val comment = it.comment()
                    val row = if (comment != null) "(${i + 1},$values) /* $comment */" else "(${i + 1},$values)"
                    ps.print(row)
                    i++
                }
                ps.println(";")
            }
        }
    }

    // S T R I N G

    @Throws(FileNotFoundException::class)
    fun insertStrings(
        items: Iterable<String>,
        resolver: (String) -> Int,
        file: File,
        table: String,
        columns: String,
        header: String,
    ) {
        PrintStream(FileOutputStream(file)).use { ps ->
            ps.println("-- $header")
            if (items.iterator().hasNext()) {
                ps.println("INSERT INTO $table ($columns) VALUES")
                var first = true
                items.forEach {
                    if (first) {
                        first = false
                    } else {
                        ps.println(",")
                    }
                    val id = resolver.invoke(it)
                    val row = "($id,'${escape(it)}')"
                    ps.print(row)
                }
                ps.println(";")
            }
        }
    }

    // G E N E R I C

    @Throws(FileNotFoundException::class)
    fun <K, V> insert(
        items: Iterable<K>,
        resolver: (K) -> V,
        file: File,
        table: String,
        columns: String,
        header: String,
        stringifier: (K, V) -> String,
    ) {
        PrintStream(FileOutputStream(file)).use { ps ->
            ps.println("-- $header")
            if (items.iterator().hasNext()) {
                ps.println("INSERT INTO $table ($columns) VALUES")
                var first = true
                items.forEach {
                    if (first) {
                        first = false
                    } else {
                        ps.println(",")
                    }
                    val v = resolver.invoke(it)
                    val values = stringifier.invoke(it, v)
                    val row = "($values)"
                    ps.print(row)
                }
                ps.println(";")
            }
        }
    }

    // R E S O L V A L B L E

    @Throws(FileNotFoundException::class)
    fun <T : Resolvable<U, R>, U, R> resolveAndInsert(
        items: Iterable<T>,
        comparator: Comparator<T>?,
        file: File,
        table: String,
        columns: String,
        header: String,
        foreignResolver: (U) -> R?,
        stringifier: (R?) -> String,
        vararg resolvedColumns: String,
    ) {
        PrintStream(FileOutputStream(file)).use { ps ->
            ps.println("-- $header")
            if (items.iterator().hasNext()) {
                ps.println("INSERT INTO $table ($columns,${resolvedColumns.joinToString(separator = ",")}) VALUES")
                var seq = items.asSequence()
                if (comparator != null) {
                    seq = seq.sortedWith(comparator)
                }
                var first = true
                seq.forEach {
                    if (first) {
                        first = false
                    } else {
                        ps.println(",")
                    }
                    val resolved = it.resolve(foreignResolver)
                    val sqlResolved = stringifier.invoke(resolved)
                    val values = it.dataRow()
                    val comment = it.comment()
                    val row = if (comment != null) "($values,$sqlResolved) /* $comment */" else "($values,$sqlResolved)"
                    ps.print(row)
                }
                ps.println(";")
            }
        }
    }

    @Throws(FileNotFoundException::class)
    fun <T : Resolvable<U, R>, U, R> resolveAndInsert(
        items: Iterable<T>,
        resolver: (T) -> Int,
        file: File,
        table: String,
        columns: String,
        header: String,
        withNumber: Boolean,
        foreignResolver: (U) -> R?,
        stringifier: (R?) -> String,
        vararg resolvedColumns: String,
    ) {
        PrintStream(FileOutputStream(file)).use { ps ->
            ps.println("-- $header")
            if (items.iterator().hasNext()) {
                ps.println("INSERT INTO $table ($columns,${resolvedColumns.joinToString(separator = ",")}) VALUES")
                var first = true
                items.forEach {
                    if (first) {
                        first = false
                    } else {
                        ps.println(",")
                    }
                    val id = resolver.invoke(it)
                    val resolved = it.resolve(foreignResolver)
                    val sqlResolved = stringifier.invoke(resolved)
                    val values = it.dataRow()
                    val comment = it.comment()
                    val row =
                        if (withNumber)
                            (if (comment != null) "($id,$values,$sqlResolved) /* $comment */" else "($id,$values,$sqlResolved)")
                        else
                            (if (comment != null) "($values,$sqlResolved) /* $comment */" else "($values,$sqlResolved)")
                    ps.print(row)
                }
                ps.println(";")
            }
        }
    }
}
