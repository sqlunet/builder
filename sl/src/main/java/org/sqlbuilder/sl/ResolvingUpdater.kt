package org.sqlbuilder.sl

import org.sqlbuilder.common.CombinedResolver
import org.sqlbuilder.common.Progress.traceDone
import org.sqlbuilder.common.Progress.tracePending
import org.sqlbuilder.common.Update
import org.sqlbuilder.common.Utils.escape
import org.sqlbuilder.common.Utils.nullableInt
import org.sqlbuilder.sl.foreign.VnClassAlias
import org.sqlbuilder.sl.foreign.VnRoleAlias
import org.sqlbuilder2.ser.Pair
import org.sqlbuilder2.ser.Triplet
import java.io.File
import java.io.FileNotFoundException
import java.util.*
import java.util.function.Function

class ResolvingUpdater(conf: Properties) : ResolvingInserter(conf) {
    init {
        // output
        this.outDir = File(conf.getProperty("sl_outdir_updated", "sql/data_updated"))
        if (!this.outDir.exists()) {
            this.outDir.mkdirs()
        }
    }

    @Throws(FileNotFoundException::class)
    override fun insert() {
        insertVnAliases()
        insertVnRoleAliases()
    }

    @Throws(FileNotFoundException::class)
    override fun insertVnAliases() {
        tracePending("set", "vnalias")
        val pbrolesetCol = names.column("pbrolesets_vnclasses.pbroleset")
        val vnclassCol = names.column("pbrolesets_vnclasses.vnclass")
        val pbrolesetidCol = names.column("pbrolesets_vnclasses.pbrolesetid")
        val vnclassidCol = names.column("pbrolesets_vnclasses.vnclassid")
        Update.update(
            VnClassAlias.SET,
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
    override fun insertVnRoleAliases() {
        tracePending("set", "vnaliasrole")
        val pbrolesetCol = names.column("pbroles_vnroles.pbroleset")
        val pbroleCol = names.column("pbroles_vnroles.pbarg")
        val vnclassCol = names.column("pbroles_vnroles.vnclass")
        val vnroleCol = names.column("pbroles_vnroles.vntheta")
        val pbrolesetidCol = names.column("pbroles_vnroles.pbrolesetid")
        val pbroleidCol = names.column("pbroles_vnroles.pbroleid")
        val vnclassidCol = names.column("pbroles_vnroles.vnclassid")
        val vnroleidCol = names.column("pbroles_vnroles.vnroleid")
        val vnroletypeidCol = names.column("pbroles_vnroles.vnroletypeid")

        val setStringifier = Function { r: Pair<Pair<Int, Int>, Triplet<Int, Int, Int>>? ->
            if (r == null)
                "$pbrolesetidCol=NULL,$pbroleidCol=NULL,$vnclassidCol=NULL,$vnroleidCol=NULL,$vnroletypeidCol=NULL"
            else {
                val v1 = if (r.first == null)
                    "$pbrolesetidCol=NULL,$pbroleidCol=NULL"
                else
                    "$pbrolesetidCol=${nullableInt(r.first.first)},$pbroleidCol=${nullableInt(r.first.second)}"
                val v2 = if (r.second == null)
                    "$vnclassidCol=NULL,$vnroleidCol=NULL,$vnroletypeidCol=NULL"
                else
                    "$vnclassidCol=${nullableInt(r.second.first)},$vnroleidCol=${nullableInt(r.second.second)},$vnroletypeidCol=${nullableInt(r.second.third)}"
                "$v1,$v2"
            }
        }
        val whereStringifier = Function { r: Pair<Pair<String, String>, Pair<String, String>>? ->
            "$pbrolesetCol='${escape(r!!.first.first)}' AND $pbroleCol='${escape(r.first.second)}' AND $vnclassCol='${escape(r.second.first)}' AND $vnroleCol='${escape(r.second.second)}'"
        }

        Update.update(
            VnRoleAlias.SET,
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
