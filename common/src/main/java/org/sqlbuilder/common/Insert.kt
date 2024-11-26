package org.sqlbuilder.common

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.PrintStream
import java.util.*
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.function.Function
import java.util.stream.StreamSupport
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
                val i = intArrayOf(0)
                items.forEach(Consumer { item: T ->
                    if (i[0] != 0) {
                        ps.print(",\n")
                    }
                    val values = item.dataRow()
                    val comment = item.comment()
                    val row = if (comment != null) String.format("(%s) /* %s */", values, comment) else String.format("(%s)", values)
                    ps.print(row)
                    i[0]++
                })
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
                var stream = StreamSupport.stream<T>(Spliterators.spliteratorUnknownSize<T>(items.iterator(), Spliterator.ORDERED), false)
                if (comparator != null) {
                    stream = stream!!.sorted(comparator)
                }
                val i = intArrayOf(0)
                stream!!.forEach { e: T ->
                    if (i[0] != 0) {
                        ps.print(",\n")
                    }
                    val values = e.dataRow()
                    val comment = e.comment()
                    val row = if (comment != null) String.format("(%s) /* %s */", values, comment) else String.format("(%s)", values)
                    ps.print(row)
                    i[0]++
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
        withNumber: Boolean,
    ) {
        PrintStream(FileOutputStream(file)).use { ps ->
            ps.println("-- $header")
            if (items.iterator().hasNext()) {
                ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns)
                val i = intArrayOf(0)
                items.forEach(Consumer { key: T ->
                    if (i[0] != 0) {
                        ps.print(",\n")
                    }
                    val id = resolver.apply(key)
                    val values = key!!.dataRow()
                    val comment = key.comment()
                    val row = if (withNumber) (if (comment != null) String.format("(%d,%s) /* %s */", id, values, comment) else String.format("(%d,%s)", id, values)) else (if (comment != null) String.format(
                        "(%s) /* %s */",
                        values,
                        comment
                    ) else String.format("(%s)", values))
                    ps.print(row)
                    i[0]++
                })
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
                var stream = StreamSupport.stream<T>(Spliterators.spliteratorUnknownSize<T>(items.iterator(), Spliterator.ORDERED), false)
                if (comparator != null) {
                    stream = stream!!.sorted(comparator)
                }
                val i = intArrayOf(1)
                stream!!.forEach { e: T ->
                    if (i[0] != 1) {
                        ps.print(",\n")
                    }
                    val values = e.dataRow()
                    val comment = e.comment()
                    val row = if (comment != null) String.format("(%d,%s) /* %s */", i[0], values, comment) else String.format("(%s)", values)
                    ps.print(row)
                    i[0]++
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
                var stream = StreamSupport.stream<T>(Spliterators.spliteratorUnknownSize<T>(items.iterator(), Spliterator.ORDERED), false)
                if (comparator != null) {
                    stream = stream!!.sorted(comparator)
                }
                val i = intArrayOf(0, 0)
                stream!!.forEach { e: T ->
                    if (i[1] == 100000) {
                        ps.println(";")
                        ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns)
                        i[1] = 0
                    }
                    if (i[1] != 0) {
                        ps.print(",\n")
                    }
                    val values = e!!.dataRow()
                    val comment = e.comment()
                    val row = if (comment != null) String.format("(%d,%s) /* %s */", i[0], values, comment) else String.format("(%s)", values)
                    ps.print(row)
                    i[0]++
                    i[1]++
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
                val i = intArrayOf(0)
                items.forEach(Consumer { key: String ->
                    if (i[0] != 0) {
                        ps.print(",\n")
                    }
                    val id = resolver.apply(key)
                    val row = String.format("(%d,'%s')", id, Utils.escape(key))
                    ps.print(row)
                    i[0]++
                })
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
                val i = intArrayOf(0)
                items.forEach(Consumer { k: K ->
                    if (i[0] != 0) {
                        ps.print(",\n")
                    }
                    val v = resolver.apply(k)
                    val values = stringifier.apply(k, v)
                    val row = String.format("(%s)", values)
                    ps.print(row)
                    i[0]++
                })
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
                var stream = StreamSupport.stream<T>(Spliterators.spliteratorUnknownSize<T>(items.iterator(), Spliterator.ORDERED), false)
                if (comparator != null) {
                    stream = stream!!.sorted(comparator)
                }
                val i = intArrayOf(0)
                stream!!.forEach { e: T ->
                    if (i[0] != 0) {
                        ps.print(",\n")
                    }
                    val resolved = e.resolve(foreignResolver)
                    val sqlResolved = stringifier.apply(resolved)
                    val values = e.dataRow()
                    val comment = e.comment()
                    val row = if (comment != null) String.format("(%s,%s) /* %s */", values, sqlResolved, comment) else String.format("(%s,%s)", values, sqlResolved)
                    ps.print(row)
                    i[0]++
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
                val i = intArrayOf(0)
                items.forEach(Consumer { key: T ->
                    if (i[0] != 0) {
                        ps.print(",\n")
                    }
                    val id = resolver.apply(key)
                    val resolved = key!!.resolve(foreignResolver)
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
                    i[0]++
                })
                ps.println(";")
            }
        }
    }
}
