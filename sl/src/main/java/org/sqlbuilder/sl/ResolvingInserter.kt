package org.sqlbuilder.sl

import org.sqlbuilder.common.CombinedResolver
import org.sqlbuilder.common.Insert
import org.sqlbuilder.common.Progress.traceDone
import org.sqlbuilder.common.Progress.tracePending
import org.sqlbuilder.sl.foreign.PbRoleSet_VnClass
import org.sqlbuilder.sl.foreign.PbRole_VnRole
import java.io.File
import java.io.FileNotFoundException
import java.util.*

typealias PbVnClassResolvable = Pair<SlPbRoleSetResolvable, SlVnClassResolvable>
typealias PbVnClassResolved = Pair<SlPbRoleSetResolved, SlVnClassResolved>
typealias PbVnRoleResolvable = Pair<SlPbRoleResolvable, SlVnRoleResolvable>
typealias PbVnRoleResolved = Pair<SlPbRoleResolved, SlVnRoleResolved>

open class ResolvingInserter(conf: Properties) : Inserter(conf) {

    protected val vnClassSerFile: String = conf.getProperty("vnclass_nids")

    protected val vnRoleSerFile: String = conf.getProperty("vnrole_nids")

    protected val pbRoleSetSerFile: String = conf.getProperty("pbroleset_nids")

    protected val pbRoleSerFile: String = conf.getProperty("pbrole_nids")

    protected val vnClassResolver: VnClassResolver = VnClassResolver(vnClassSerFile)

    protected val vnRoleResolver: VnRoleResolver = VnRoleResolver(vnRoleSerFile)

    protected val pbRoleSetResolver: PbRoleSetResolver = PbRoleSetResolver(pbRoleSetSerFile)

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
    override fun insertClassAliases() {
        tracePending("set", "vnalias")
        Insert.resolveAndInsert(
            PbRoleSet_VnClass.SET,
            PbRoleSet_VnClass.COMPARATOR,
            File(outDir, names.file("pbrolesets_vnclasses")),
            names.table("pbrolesets_vnclasses"),
            names.columns("pbrolesets_vnclasses"),
            header,
            CombinedResolver<SlPbRoleSetResolvable, SlPbRoleSetResolved, SlVnClassResolvable, SlVnClassResolved>(pbRoleSetResolver, vnClassResolver),
            PbRoleSet_VnClass.RESOLVE_RESULT_STRINGIFIER,
            names.column("pbrolesets_vnclasses.pbrolesetid"),
            names.column("pbrolesets_vnclasses.vnclassid")
        )
        traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertRoleAliases() {
        tracePending("set", "vnaliasrole")
        Insert.resolveAndInsert(
            PbRole_VnRole.SET,
            PbRole_VnRole.COMPARATOR,
            File(outDir, names.file("pbroles_vnroles")),
            names.table("pbroles_vnroles"),
            names.columns("pbroles_vnroles"),
            header,
            CombinedResolver<SlPbRoleResolvable, SlPbRoleResolved, SlVnRoleResolvable, SlVnRoleResolved>(pbRoleResolver, vnRoleResolver),
            PbRole_VnRole.RESOLVE_RESULT_STRINGIFIER,
            names.column("pbroles_vnroles.pbroleid"),
            names.column("pbroles_vnroles.pbrolesetid"),
            names.column("pbroles_vnroles.vnroleid"),
            names.column("pbroles_vnroles.vnclassid"),
            names.column("pbroles_vnroles.vnroletypeid")
        )
        traceDone()
    }
}
