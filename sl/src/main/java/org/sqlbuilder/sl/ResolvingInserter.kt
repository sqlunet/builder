package org.sqlbuilder.sl

import org.sqlbuilder.common.Bunch
import org.sqlbuilder.common.CombinedResolver
import org.sqlbuilder.common.Insert
import org.sqlbuilder.common.Progress.traceDone
import org.sqlbuilder.common.Progress.tracePending
import org.sqlbuilder.sl.foreign.VnClassAlias
import org.sqlbuilder.sl.foreign.VnRoleAlias
import java.io.File
import java.io.FileNotFoundException
import java.util.*
import java.util.function.Function

typealias PbVnClassResolvable = Pair<PbRoleSetResolvable, VnClassResolvable>
typealias PbVnClassResolved = Pair<PbRoleSetResolved, VnClassResolved>
typealias PbVnRoleResolvable = Pair<PbRoleResolvable, VnRoleResolvable>
typealias PbVnRoleResolved = Pair<PbRoleResolved, VnRoleResolved>

typealias BunchPbVnClassResolvable = Bunch<PbRoleSetResolvable, VnClassResolvable>
typealias BunchPbVnClassResolved = Bunch<PbRoleSetResolved, VnClassResolved>
typealias BunchPbVnRoleResolvable = Bunch<PbRoleResolvable, VnRoleResolvable>
typealias BunchPbVnRoleResolved = Bunch<PbRoleResolved, VnRoleResolved>

open class ResolvingInserter(conf: Properties) : Inserter(conf) {

    protected val vnClassSerFile: String = conf.getProperty("vnclass_nids")

    protected val vnRoleSerFile: String = conf.getProperty("vnrole_nids")

    protected val pbRoleSetSerFile: String = conf.getProperty("pbroleset_nids")

    protected val pbRoleSerFile: String = conf.getProperty("pbrole_nids")

    @JvmField
    protected val vnClassResolver: VnClassResolver = VnClassResolver(vnClassSerFile)

    @JvmField
    protected val vnRoleResolver: VnRoleResolver = VnRoleResolver(vnRoleSerFile)

    @JvmField
    protected val pbRoleSetResolver: PbRoleSetResolver = PbRoleSetResolver(pbRoleSetSerFile)

    @JvmField
    protected val pbRoleResolver: PbRoleResolver = PbRoleResolver(pbRoleSerFile)

    init {
        // header
        header += "\n-- ${conf.getProperty("vn_resolve_against")}"
        header += "\n-- ${conf.getProperty("pb_resolve_against")}"

        // output
        outDir = File(conf.getProperty("sl_outdir_resolved", "sql/data_resolved"))
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
    }

    @Throws(FileNotFoundException::class)
    override fun insertVnAliases() {
        tracePending("set", "vnalias")
        val cr = CombinedResolver<PbRoleSetResolvable, PbRoleSetResolved, VnClassResolvable, VnClassResolved>(pbRoleSetResolver, vnClassResolver)
        val cr2: Function<PbVnClassResolvable, PbVnClassResolved?> = cr
        //val bcr = CombinedResolver2<PbRoleSetResolvable, PbRoleSetResolved, VnClassResolvable, VnClassResolved>(pbRoleSetResolver, vnClassResolver)
        //val bcr2: Function<BunchPbVnClassResolvable, BunchPbVnClassResolved?> = bcr
        Insert.resolveAndInsert<VnClassAlias, PbRoleSetResolvable, PbVnClassResolved>(
            VnClassAlias.SET,
            VnClassAlias.COMPARATOR,
            File(outDir, names.file("pbrolesets_vnclasses")),
            names.table("pbrolesets_vnclasses"),
            names.columns("pbrolesets_vnclasses"),
            header,
            cr2,
            VnClassAlias.RESOLVE_RESULT_STRINGIFIER,
            names.column("pbrolesets_vnclasses.pbrolesetid"),
            names.column("pbrolesets_vnclasses.vnclassid")
        )
        traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertVnRoleAliases() {
        tracePending("set", "vnaliasrole")
        val cr = CombinedResolver<PbRoleResolvable, PbRoleResolved, VnRoleResolvable, VnRoleResolved>(pbRoleResolver, vnRoleResolver)
        val cr2: Function<PbVnRoleResolvable, PbVnRoleResolved?> = cr
        Insert.resolveAndInsert<VnRoleAlias, PbVnRoleResolvable, PbVnRoleResolved>(
            VnRoleAlias.SET,
            VnRoleAlias.COMPARATOR,
            File(outDir, names.file("pbroles_vnroles")),
            names.table("pbroles_vnroles"),
            names.columns("pbroles_vnroles"),
            header,
            cr2, //CombinedResolver(pbRoleResolver, vnRoleResolver),
            VnRoleAlias.RESOLVE_RESULT_STRINGIFIER,
            names.column("pbroles_vnroles.pbroleid"),
            names.column("pbroles_vnroles.pbrolesetid"),
            names.column("pbroles_vnroles.vnroleid"),
            names.column("pbroles_vnroles.vnclassid"),
            names.column("pbroles_vnroles.vnroletypeid")
        )
        traceDone()
    }
}
