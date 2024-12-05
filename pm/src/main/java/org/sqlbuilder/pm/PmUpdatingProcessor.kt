package org.sqlbuilder.pm

import org.sqlbuilder.common.Utils.escape
import org.sqlbuilder.common.Utils.nullable
import org.sqlbuilder.common.Utils.nullableInt
import org.sqlbuilder.common.Utils.quote
import org.sqlbuilder.pm.objects.PmEntry
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.util.*

class PmUpdatingProcessor(conf: Properties) : PmResolvingProcessor(conf) {
    init {
        // output
        this.outDir = File(conf.getProperty("pm_outdir_updated", "sql/data_updated"))
        if (!this.outDir.exists()) {
            this.outDir.mkdirs()
        }
    }

    @Throws(IOException::class)
    override fun run() {
        val inputFile = File(pMHome, pMFile)
        val singleOutput = names.updateFile("pms")
        val wnOutput = names.updateFileNullable("pms.wn")
        val xnOutput = names.updateFileNullable("pms.xn")

        if (wnOutput == null || xnOutput == null) {
            PrintStream(FileOutputStream(File(outDir, singleOutput)), true, StandardCharsets.UTF_8).use { ps ->
                ps.println("-- $header")
                processPmFile(inputFile, makeWnConsumer(ps))
                processPmFile(inputFile, makeXnConsumer(ps))
            }
        } else {
            PrintStream(FileOutputStream(File(outDir, wnOutput)), true, StandardCharsets.UTF_8).use { ps ->
                ps.println("-- $header")
                processPmFile(inputFile, makeWnConsumer(ps))
            }
            PrintStream(FileOutputStream(File(outDir, xnOutput)), true, StandardCharsets.UTF_8).use { ps ->
                ps.println("-- $header")
                processPmFile(inputFile, makeXnConsumer(ps))
            }
        }
    }

    private fun updateWordSenseRow(ps: PrintStream, table: String, index: Int, entry: PmEntry, vararg columns: String) {
        val wordid = wordResolver.invoke(entry.word!!)
        val wordResolved = nullableInt(wordid)
        val sk = sensekeyResolver.invoke(entry.sensekey!!)
        val senseResolved = nullable(sk) { nullableInt(it.second) }

        val setClause = "${columns[0]}=$wordResolved,${columns[1]}=$senseResolved"
        val whereClause = "${columns[2]}=${quote(escape(entry.word!!))} AND ${columns[3]}=${nullable(entry.sensekey) { quote(escape(escape(it))) }}"
        ps.println("UPDATE $table SET $setClause WHERE $whereClause; -- ${index + 1}")
    }

    private fun updateVnPbFnRow(ps: PrintStream, table: String, index: Int, entry: PmEntry, vararg columns: String) {
        val vnWordid = vnWordResolver.invoke(entry.word!!)
        val pbWordid = pbWordResolver.invoke(entry.word!!)
        val fnWordid = fnWordResolver.invoke(entry.word!!)
        val vn = vnRoleResolver.invoke(PmVnRoleResolvable(entry.vn.clazz!!, entry.vn.role!!))
        val pb = pbRoleResolver.invoke(PmPbRoleResolvable(entry.pb.roleset!!, entry.pb.arg!!))
        val fn = fnRoleResolver.invoke(PmFnRoleResolvable(entry.fn.frame!!, entry.fn.fetype!!))

        val setClause =
            "${columns[0]}=${nullableInt(vnWordid)},${columns[1]}=${nullableInt(pbWordid)},${columns[2]}=${nullableInt(fnWordid)},${columns[3]}=${nullable(vn) { nullableInt(it.first) }},${columns[4]}=${nullable(vn) { nullableInt(it.second) }},${columns[5]}=${
                nullable(pb) { nullableInt(it.first) }
            },${columns[6]}=${nullable(pb) { nullableInt(it.second) }},${columns[7]}=${nullable(fn) { nullableInt(it.first) }},${columns[8]}=${nullable(fn) { nullableInt(it.second) }},${columns[9]}=${nullable(fn) { nullableInt(it.third) }}"
        val whereClause = "${columns[10]}=${nullable(entry.word) { quote(escape(it)) }} AND ${columns[11]}=${nullable(entry.vn.clazz) { quote(escape(it)) }} AND ${columns[12]}=${nullable(entry.vn.role) { quote(escape(it)) }} AND ${columns[13]}=${
            nullable(entry.pb.roleset) {
                quote(escape(it))
            }
        } AND ${columns[14]}=${nullable(entry.pb.arg) { quote(escape(it)) }} AND ${columns[15]}=${nullable(entry.fn.frame) { quote(escape(it)) }} AND ${columns[16]}=${nullable(entry.fn.fetype) { quote(escape(it)) }}"
        ps.println("UPDATE $table SET $setClause WHERE $whereClause; -- ${index + 1}")
    }

    private fun makeWnConsumer(ps: PrintStream): (PmEntry, Int) -> Unit {
        return { entry, i ->
            updateWordSenseRow(
                ps,
                names.table("pms"), i, entry,
                names.column("pms.wordid"),
                names.column("pms.synsetid"),
                names.column("pms.word"),
                names.column("pms.sensekey")
            )
        }
    }

    private fun makeXnConsumer(ps: PrintStream): (PmEntry, Int) -> Unit {
        return { entry, i ->
            updateVnPbFnRow(
                ps,
                names.table("pms"), i, entry,
                names.column("pms.vnwordid"),
                names.column("pms.pbwordid"),
                names.column("pms.fnwordid"),
                names.column("pms.vnclassid"),
                names.column("pms.vnroleid"),
                names.column("pms.pbrolesetid"),
                names.column("pms.pbroleid"),
                names.column("pms.fnframeid"),
                names.column("pms.fnfeid"),
                names.column("pms.fnluid"),
                names.column("pms.word"),
                names.column("pms.vnclass"),
                names.column("pms.vnrole"),
                names.column("pms.pbroleset"),
                names.column("pms.pbrole"),
                names.column("pms.fnframe"),
                names.column("pms.fnfe")
            )
        }
    }

    @Throws(IOException::class)
    private fun processPmFile(file: File, consumer: (PmEntry, Int) -> Unit) {
        process(file, { PmEntry.Companion.parse(it) }, consumer)
    }
}
