package org.sqlbuilder.common

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.PrintStream
import java.util.function.BiFunction
import java.util.function.Function
import kotlin.Throws

object Insert {

    // I N S E R T A B L E S

    @Throws(FileNotFoundException::class)
    @JvmStatic
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
                ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns)
                var first = true
                items.forEach { item: T ->
                    if (first) {
                        first = false
                    } else {
                        ps.println(",")
                    }
                    val values = item.dataRow()
                    val comment = item.comment()
                    val row = if (comment != null) String.format("(%s) /* %s */", values, comment) else String.format("(%s)", values)
                    ps.print(row)
                }
                ps.println(";")
            }
        }
    }

    @Throws(FileNotFoundException::class)
    @JvmStatic
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
                ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns)
                var seq = items.asSequence()
                if (comparator != null) {
                    seq = seq.sortedWith(comparator)
                }
                var first = true
                seq.forEach { e: T ->
                    if (first) {
                        first = false
                    } else {
                        ps.println(",")
                    }
                    val values = e.dataRow()
                    val comment = e.comment()
                    val row = if (comment != null) String.format("(%s) /* %s */", values, comment) else String.format("(%s)", values)
                    ps.print(row)
                }
                ps.println(";")
            }
        }
    }

    @Throws(FileNotFoundException::class)
    @JvmStatic
    fun <T : Insertable> insert(
        items: Iterable<T>,
        resolver: Function<T, Int>,
        file: File,
        table: String,
        columns: String,
        header: String,
    ) {
        insert<T>(items, resolver, file, table, columns, header, true)
    }

    @Throws(FileNotFoundException::class)
    @JvmStatic
    fun <T : Insertable> insert(
        items: Iterable<T>,
        resolver: Function<T, Int>,
        file: File,
        table: String,
        columns: String,
        header: String,
        withNumber: Boolean = true,
    ) {
        PrintStream(FileOutputStream(file)).use { ps ->
            ps.println("-- $header")
            if (items.iterator().hasNext()) {
                ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns)
                var first = true
                items.forEach { key: T ->
                    if (first) {
                        first = false
                    } else {
                        ps.println(",")
                    }
                    val id = resolver.apply(key)
                    val values = key.dataRow()
                    val comment = key.comment()
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
    @JvmStatic
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
                ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns)
                var seq = items.asSequence()
                if (comparator != null) {
                    seq = seq.sortedWith(comparator)
                }
                var i = 0
                seq.forEach { e: T ->
                    if (i > 0) {
                        ps.println(",")
                    }
                    val values = e.dataRow()
                    val comment = e.comment()
                    val row = if (comment != null) String.format("(%d,%s) /* %s */", i, values, comment) else String.format("(%s)", values)
                    ps.print(row)
                    i++
                }
                ps.println(";")
            }
        }
    }

    @Throws(FileNotFoundException::class)
    @JvmStatic
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
                ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns)
                var seq = items.asSequence()
                if (comparator != null) {
                    seq = seq.sortedWith(comparator)
                }
                var i = 0
                seq.forEach { e: T ->
                    if (i == 100000) {
                        ps.println(";")
                        ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns)
                        i = 0
                    }
                    if (i > 0) {
                        ps.println(",")
                    }
                    val values = e.dataRow()
                    val comment = e.comment()
                    val row = if (comment != null) String.format("(%d,%s) /* %s */", i, values, comment) else String.format("(%s)", values)
                    ps.print(row)
                    i++
                }
                ps.println(";")
            }
        }
    }

    // S T R I N G

    @Throws(FileNotFoundException::class)
    @JvmStatic
    fun insertStrings(
        items: Iterable<String>,
        resolver: Function<String, Int>,
        file: File,
        table: String,
        columns: String,
        header: String,
    ) {
        PrintStream(FileOutputStream(file)).use { ps ->
            ps.println("-- $header")
            if (items.iterator().hasNext()) {
                ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns)
                var first = true
                items.forEach { key: String ->
                    if (first) {
                        first = false
                    } else {
                        ps.println("")
                    }
                    val id = resolver.apply(key)
                    val row = String.format("(%d,'%s')", id, Utils.escape(key))
                    ps.print(row)
                }
                ps.println(";")
            }
        }
    }

    // G E N E R I C

    @Throws(FileNotFoundException::class)
    @JvmStatic
    fun <K, V> insert(
        items: Iterable<K>,
        resolver: Function<K, V>,
        file: File,
        table: String,
        columns: String,
        header: String,
        stringifier: BiFunction<K, V, String>,
    ) {
        PrintStream(FileOutputStream(file)).use { ps ->
            ps.println("-- $header")
            if (items.iterator().hasNext()) {
                ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns)
                var first = true
                items.forEach { k: K ->
                    if (first) {
                        first = false
                    } else {
                        ps.println("")
                    }
                    val v = resolver.apply(k)
                    val values = stringifier.apply(k, v)
                    val row = String.format("(%s)", values)
                    ps.print(row)
                }
                ps.println(";")
            }
        }
    }

    // R E S O L V A L B L E

    @Throws(FileNotFoundException::class)
    @JvmStatic
    fun <T : Resolvable<U, R>, U, R> resolveAndInsert(
        items: Iterable<T>,
        comparator: Comparator<T>?,
        file: File,
        table: String,
        columns: String,
        header: String,
        foreignResolver: Function<U, R>,
        stringifier: Function<R, String>,
        vararg resolvedColumns: String,
    ) {
        PrintStream(FileOutputStream(file)).use { ps ->
            ps.println("-- $header")
            if (items.iterator().hasNext()) {
                ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns + "," + resolvedColumns.joinToString(separator = ","))
                var seq = items.asSequence()
                if (comparator != null) {
                    seq = seq.sortedWith(comparator)
                }
                var first = true
                seq.forEach { e: T ->
                    if (first) {
                        first = false
                    } else {
                        ps.println("")
                    }
                    val resolved = e.resolve(foreignResolver)
                    val sqlResolved = stringifier.apply(resolved)
                    val values = e.dataRow()
                    val comment = e.comment()
                    val row = if (comment != null) String.format("(%s,%s) /* %s */", values, sqlResolved, comment) else String.format("(%s,%s)", values, sqlResolved)
                    ps.print(row)
                }
                ps.println(";")
            }
        }
    }

    @Throws(FileNotFoundException::class)
    @JvmStatic
    fun <T : Resolvable<U, R>, U, R> resolveAndInsert(
        items: Iterable<T>,
        resolver: Function<T, Int>,
        file: File,
        table: String,
        columns: String,
        header: String,
        withNumber: Boolean,
        foreignResolver: Function<U, R>,
        stringifier: Function<R, String>,
        vararg resolvedColumns: String,
    ) {
        PrintStream(FileOutputStream(file)).use { ps ->
            ps.println("-- $header")
            if (items.iterator().hasNext()) {
                ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns + "," + resolvedColumns.joinToString(separator = ","))
                var first = true
                items.forEach { key: T ->
                    if (first) {
                        first = false
                    } else {
                        ps.println("")
                    }
                    val id = resolver.apply(key)
                    val resolved = key.resolve(foreignResolver)
                    val sqlResolved = stringifier.apply(resolved)
                    val values = key.dataRow()
                    val comment = key.comment()
                    val row = if (withNumber) (if (comment != null) String.format("(%d,%s,%s) /* %s */", id, values, sqlResolved, comment) else String.format(
                        "(%d,%s,%s)",
                        id,
                        values,
                        sqlResolved
                    )) else (if (comment != null) String.format("(%s,%s) /* %s */", values, sqlResolved, comment) else String.format("(%s,%s)", values, sqlResolved))
                    ps.print(row)
                }
                ps.println(";")
            }
        }
    }
}
