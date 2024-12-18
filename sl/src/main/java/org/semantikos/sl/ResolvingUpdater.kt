package org.semantikos.sl

import org.semantikos.common.CombinedResolver
import org.semantikos.common.Progress.traceDone
import org.semantikos.common.Progress.traceSaving
import org.semantikos.common.Update.update
import org.semantikos.common.Utils.escape
import org.semantikos.common.Utils.nullableInt
import org.semantikos.sl.foreign.PbRoleSet_VnClass
import org.semantikos.sl.foreign.PbRole_VnRole
import java.io.File
import java.io.FileNotFoundException
import java.util.*

class ResolvingUpdater(conf: Properties) : ResolvingInserter(conf) {
    init {
        // output
        outDir = File(conf.getProperty("sl_outdir_updated", "sql/data_updated"))
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
    }

    @Throws(FileNotFoundException::class)
    override fun insert() {
        insertClassAliases()
        insertRoleAliases()
    }

    @Throws(FileNotFoundException::class)
    override fun insertClassAliases() {
        traceSaving("vnalias")
        val pbrolesetCol = names.column("pbrolesets_vnclasses.pbroleset")
        val vnclassCol = names.column("pbrolesets_vnclasses.vnclass")
        val pbrolesetidCol = names.column("pbrolesets_vnclasses.pbrolesetid")
        val vnclassidCol = names.column("pbrolesets_vnclasses.vnclassid")
        update(
            PbRoleSet_VnClass.SET,
            File(outDir, names.updateFile("pbrolesets_vnclasses")),
            header,
            names.table("pbrolesets_vnclasses"),
            CombinedResolver(pbRoleSetResolver, vnClassResolver),
            { resolved -> if (resolved == null) "$pbrolesetidCol=NULL,$vnclassidCol=NULL" else "$pbrolesetidCol=${nullableInt(resolved.first)},$vnclassidCol=${nullableInt(resolved.second)}" },
            { resolving -> "$pbrolesetCol='${escape(resolving.first)}' AND $vnclassCol='${escape(resolving.second)}'" }
        )
        traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertRoleAliases() {
        traceSaving("vnaliasrole")
        val pbrolesetCol = names.column("pbroles_vnroles.pbroleset")
        val pbroleCol = names.column("pbroles_vnroles.pbarg")
        val vnclassCol = names.column("pbroles_vnroles.vnclass")
        val vnroleCol = names.column("pbroles_vnroles.vntheta")
        val pbrolesetidCol = names.column("pbroles_vnroles.pbrolesetid")
        val pbroleidCol = names.column("pbroles_vnroles.pbroleid")
        val vnclassidCol = names.column("pbroles_vnroles.vnclassid")
        val vnroleidCol = names.column("pbroles_vnroles.vnroleid")
        val vnroletypeidCol = names.column("pbroles_vnroles.vnroletypeid")

        val setStringifier = { r: PbVnRoleResolved? ->
            if (r == null)
                "$pbrolesetidCol=NULL,$pbroleidCol=NULL,$vnclassidCol=NULL,$vnroleidCol=NULL,$vnroletypeidCol=NULL"
            else {
                val v1 = "$pbrolesetidCol=${nullableInt(r.first.first)},$pbroleidCol=${nullableInt(r.first.second)}"
                val v2 = "$vnclassidCol=${nullableInt(r.second.first)},$vnroleidCol=${nullableInt(r.second.second)},$vnroletypeidCol=${nullableInt(r.second.third)}"
                "$v1,$v2"
            }
        }
        val whereStringifier = { r: PbVnRoleResolvable ->
            "$pbrolesetCol='${escape(r.first.first)}' AND $pbroleCol='${escape(r.first.second)}' AND $vnclassCol='${escape(r.second.first)}' AND $vnroleCol='${escape(r.second.second)}'"
        }

        update(
            PbRole_VnRole.SET,
            File(outDir, names.updateFile("pbroles_vnroles")),
            header,
            names.table("pbroles_vnroles"),
            CombinedResolver(pbRoleResolver, vnRoleResolver),
            setStringifier,
            whereStringifier
        )
        traceDone()
    }
}
