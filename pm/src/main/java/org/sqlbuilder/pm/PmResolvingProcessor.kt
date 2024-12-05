package org.sqlbuilder.pm

import org.sqlbuilder.common.Insert
import org.sqlbuilder.common.Insert.insert
import org.sqlbuilder.common.Names
import org.sqlbuilder.common.Utils.nullableInt
import org.sqlbuilder.pm.objects.PmEntry
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

    @JvmField
    protected val wordResolver: WordResolver = WordResolver(wordSerFile)

    @JvmField
    protected val vnWordResolver: VnWordResolver = VnWordResolver(vnWordSerFile)

    @JvmField
    protected val pbWordResolver: PbWordResolver = PbWordResolver(pbWordSerFile)

    @JvmField
    protected val fnWordResolver: FnWordResolver = FnWordResolver(fnWordSerFile)

    @JvmField
    protected val sensekeyResolver: SensekeyResolver = SensekeyResolver(sensekeySerFile)

    @JvmField
    protected val vnRoleResolver: VnRoleResolver = VnRoleResolver(vnRoleSerFile)

    @JvmField
    protected val pbRoleResolver: PbRoleResolver = PbRoleResolver(pbRoleSerFile)

    @JvmField
    protected val fnRoleResolver: FnRoleResolver = FnRoleResolver(fnRoleSerFile)

    protected val fnLexUnitResolver: FnLexUnitResolver = FnLexUnitResolver(fnLexUnitSerFile)

    override var outDir: File = File(conf.getProperty("pm_outdir_resolved", "sql/data_resolved"))

    init {

        // header
        header += "\n-- " + conf.getProperty("wn_resolve_against")
        header += "\n-- " + conf.getProperty("vn_resolve_against")
        header += "\n-- " + conf.getProperty("pb_resolve_against")

        // output
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
    }

    @Throws(IOException::class)
    override fun run() {
        val inputFile = File(pMHome, pMFile)
        process(inputFile, { parse(it) }, null)
        PmPredicate.COLLECTOR.open().use {
            insert(PmPredicate.COLLECTOR, PmPredicate.COLLECTOR, File(outDir, names.file("predicates")), names.table("predicates"), names.columns("predicates"), header)
            PmRole.COLLECTOR.open().use {
                insert(PmRole.COLLECTOR, PmRole.COLLECTOR, File(outDir, names.file("roles")), names.table("roles"), names.columns("roles"), header)
                PrintStream(FileOutputStream(File(outDir, names.file("pms"))), true, StandardCharsets.UTF_8).use { ps ->
                    ps.println("-- $header")
                    processPmFile(ps, inputFile, names.table("pms"), names.columns("pms", true), { entry, i ->
                        val wordid = wordResolver.invoke(entry.word!!)
                        val sk = sensekeyResolver.invoke(entry.sensekey!!)

                        val vnWordid = vnWordResolver.invoke(entry.word!!)
                        val vn = vnRoleResolver.invoke(PmVnRoleResolvable(entry.vn.clazz!!, entry.vn.role!!))
                        val pbWordid = pbWordResolver.invoke(entry.word!!)
                        val pb = pbRoleResolver.invoke(PmPbRoleResolvable(entry.pb.roleset!!, entry.pb.arg!!))
                        val fnWordid = fnWordResolver.invoke(entry.word!!)
                        val fn = fnRoleResolver.invoke(PmFnRoleResolvable(entry.fn.frame!!, entry.fn.fetype!!))
                        val fnLu = fnLexUnitResolver.invoke(PmFnLexUnitResolvable(entry.fn.frame!!, entry.fn.lu!!))

                        val wordResolved = nullableInt(wordid)
                        val senseResolved = if (sk == null) "NULL" else nullableInt(sk.second)

                        val vnWordResolved = nullableInt(vnWordid)
                        val pbWordResolved = nullableInt(pbWordid)
                        val fnWordResolved = nullableInt(fnWordid)
                        val vnResolved = if (vn == null) "NULL,NULL" else "${nullableInt(vn.second)},${nullableInt(vn.first)}"
                        val pbResolved = if (pb == null) "NULL,NULL" else "${nullableInt(pb.second)},${nullableInt(pb.first)}"
                        val fnResolved = if (fn == null) "NULL,NULL,NULL" else "${nullableInt(fn.second)},${nullableInt(fn.first)},${nullableInt(fn.third)}"
                        val fnLuResolved = if (fnLu == null) "NULL" else nullableInt(fnLu.first)
                        insertRow(ps, i!!, "${entry.dataRow()},${wordResolved},${vnWordResolved},${pbWordResolved},${fnWordResolved},${senseResolved},${vnResolved},${pbResolved},${fnResolved},${fnLuResolved}")
                    })
                }
            }
        }
    }
}
