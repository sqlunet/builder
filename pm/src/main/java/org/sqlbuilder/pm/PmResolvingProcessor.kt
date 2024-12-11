package org.sqlbuilder.pm

import org.sqlbuilder.common.Insert.insert
import org.sqlbuilder.common.Names
import org.sqlbuilder.common.Progress.traceDone
import org.sqlbuilder.common.Progress.traceSaving
import org.sqlbuilder.common.Utils.nullableInt
import org.sqlbuilder.pm.objects.PmEntry.Companion.parse
import org.sqlbuilder.pm.objects.PmPredicate
import org.sqlbuilder.pm.objects.PmRole
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.util.*

open class PmResolvingProcessor(conf: Properties) : PmProcessor(conf) {

    override val names: Names = Names("pm")

    protected val wordSerFile: String = conf.getProperty("word_nids")

    protected val vnWordSerFile: String = conf.getProperty("vnword_nids")

    protected val pbWordSerFile: String = conf.getProperty("pbrole_nids")

    protected val fnWordSerFile: String = conf.getProperty("fnrole_nids")

    protected val sensekeySerFile: String = conf.getProperty("sense_nids")

    protected val vnRoleSerFile: String = conf.getProperty("vnrole_nids")

    protected val pbRoleSerFile: String = conf.getProperty("pbword_nids")

    protected val fnRoleSerFile: String = conf.getProperty("fnword_nids")

    protected val fnLexUnitSerFile: String = conf.getProperty("fnlexunit_nids")

    protected val wordResolver: WordResolver = WordResolver(wordSerFile)

    protected val vnWordResolver: VnWordResolver = VnWordResolver(vnWordSerFile)

    protected val pbWordResolver: PbWordResolver = PbWordResolver(pbWordSerFile)

    protected val fnWordResolver: FnWordResolver = FnWordResolver(fnWordSerFile)

    protected val sensekeyResolver: SensekeyResolver = SensekeyResolver(sensekeySerFile)

    protected val vnRoleResolver: VnRoleResolver = VnRoleResolver(vnRoleSerFile)

    protected val pbRoleResolver: PbRoleResolver = PbRoleResolver(pbRoleSerFile)

    protected val fnRoleResolver: FnRoleResolver = FnRoleResolver(fnRoleSerFile)

    protected val fnLexUnitResolver: FnLexUnitResolver = FnLexUnitResolver(fnLexUnitSerFile)

    init {

        // header
        header += "\n-- " + conf.getProperty("wn_resolve_against")
        header += "\n-- " + conf.getProperty("vn_resolve_against")
        header += "\n-- " + conf.getProperty("pb_resolve_against")

        // output
        outDir = File(conf.getProperty("pm_outdir_resolved", "sql/data_resolved"))
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
    }

    @Throws(IOException::class)
    override fun run() {
        val inputFile = File(pMHome, pMFile)
        process(inputFile, { parse(it) }, null)
        PmPredicate.COLLECTOR.open().use {
            traceSaving("pm", "predicates")
            insert(PmPredicate.COLLECTOR, PmPredicate.COLLECTOR, File(outDir, names.file("predicates")), names.table("predicates"), names.columns("predicates"), header)
            traceDone()

            PmRole.COLLECTOR.open().use {
                traceSaving("pm", "roles")
                insert(PmRole.COLLECTOR, PmRole.COLLECTOR, File(outDir, names.file("roles")), names.table("roles"), names.columns("roles"), header)
                traceDone()

                traceSaving("pm", "pms")
                PrintStream(FileOutputStream(File(outDir, names.file("pms"))), true, StandardCharsets.UTF_8).use { ps ->
                    ps.println("-- $header")
                    processPmFile(ps, inputFile, names.table("pms"), names.columns("pms", true)) { entry, i ->
                        val wordid = if (entry.word == null) null else wordResolver.invoke(entry.word!!)
                        val sk = if (entry.sensekey == null) null else sensekeyResolver.invoke(entry.sensekey!!)

                        val vnWordid = if (entry.word == null) null else vnWordResolver.invoke(entry.word!!)
                        val pbWordid = if (entry.word == null) null else pbWordResolver.invoke(entry.word!!)
                        val fnWordid = if (entry.word == null) null else fnWordResolver.invoke(entry.word!!)
                        val fnLu = if (entry.fn.frame == null || entry.fn.lu == null) null else fnLexUnitResolver.invoke(PmFnLexUnitResolvable(entry.fn.frame!!, entry.fn.lu!!))

                        val vn = if (entry.vn.clazz == null || entry.vn.role == null) null else vnRoleResolver.invoke(PmVnRoleResolvable(entry.vn.clazz!!, entry.vn.role!!))
                        val pb = if (entry.pb.roleset == null || entry.pb.arg == null) null else pbRoleResolver.invoke(PmPbRoleResolvable(entry.pb.roleset!!, entry.pb.arg!!))
                        val fn = if (entry.fn.frame == null || entry.fn.fetype == null) null else fnRoleResolver.invoke(PmFnRoleResolvable(entry.fn.frame!!, entry.fn.fetype!!))

                        val wordResolved = nullableInt(wordid)
                        val senseResolved = if (sk == null) "NULL" else nullableInt(sk.second)

                        val vnWordResolved = nullableInt(vnWordid)
                        val pbWordResolved = nullableInt(pbWordid)
                        val fnWordResolved = nullableInt(fnWordid)

                        val vnResolved = if (vn == null) "NULL,NULL" else "${nullableInt(vn.second)},${nullableInt(vn.first)}"
                        val pbResolved = if (pb == null) "NULL,NULL" else "${nullableInt(pb.second)},${nullableInt(pb.first)}"
                        val fnResolved = if (fn == null) "NULL,NULL,NULL" else "${nullableInt(fn.second)},${nullableInt(fn.first)},${nullableInt(fn.third)}"
                        val fnLuResolved = if (fnLu == null) "NULL" else nullableInt(fnLu.first)

                        insertRow(ps, i, "${entry.dataRow()},${wordResolved},${vnWordResolved},${pbWordResolved},${fnWordResolved},${senseResolved},${vnResolved},${pbResolved},${fnResolved},${fnLuResolved}")
                    }
                }
                traceDone()
            }
        }
    }
}
