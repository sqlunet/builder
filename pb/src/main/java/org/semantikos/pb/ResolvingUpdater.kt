package org.semantikos.pb

import org.semantikos.common.Progress.traceDone
import org.semantikos.common.Progress.traceSaving
import org.semantikos.common.Update.update
import org.semantikos.common.Utils.escape
import org.semantikos.common.Utils.nullableInt
import org.semantikos.pb.foreign.RoleSetToFn
import org.semantikos.pb.foreign.RoleSetToVn
import org.semantikos.pb.foreign.RoleToVn
import org.semantikos.pb.objects.Word
import java.io.File
import java.io.FileNotFoundException
import java.util.*

class ResolvingUpdater(conf: Properties) : ResolvingInserter(conf) {
    init {
        // output
        outDir = File(conf.getProperty("pm_outdir_updated", "sql/data_updated"))
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
    }

    @Throws(FileNotFoundException::class)
    override fun insert() {
        Word.COLLECTOR.open().use {
            insertWords()
            insertFnFrameAliases()
            insertVnClassAliases()
            insertVnRoleAliases()
            insertFnFeAliases()
        }
    }

    @Throws(FileNotFoundException::class)
    override fun insertWords() {
        traceSaving("word")
        val wordidCol = names.column("words.wordid")
        val wordCol = names.column("words.word")
        update(
            Word.COLLECTOR,
            File(outDir, names.updateFile("words")),
            header,
            names.table("words"),
            wordResolver,
            { resolved -> "$wordidCol=${nullableInt(resolved)}" },
            { resolving -> "$wordCol='${escape(resolving)}'" })
        traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertVnClassAliases() {
        traceSaving("vnclass aliases")
        val vnclassidCol = names.column("pbrolesets_vnclasses.vnclassid")
        val vnclassCol = names.column("pbrolesets_vnclasses.vnclass")
        update(
            RoleSetToVn.SET,
            File(outDir, names.updateFile("pbrolesets_vnclasses")),
            header,
            names.table("pbrolesets_vnclasses"),
            vnClassResolver,
            { resolved -> "$vnclassidCol=${nullableInt(resolved)}" },
            { resolving -> "$vnclassCol='${escape(resolving)}'" })
        traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertFnFrameAliases() {
        traceSaving("fnframe aliases")
        val fnframeidCol = names.column("pbrolesets_fnframes.fnframeid")
        val vnclassCol = names.column("pbrolesets_fnframes.fnframe")
        update(
            RoleSetToFn.SET,
            File(outDir, names.updateFile("pbrolesets_fnframes")),
            header,
            names.table("pbrolesets_fnframes"),
            fnFrameResolver,
            { resolved -> "$fnframeidCol=${nullableInt(resolved)}" },
            { resolving -> "$vnclassCol='${escape(resolving)}'" })
        traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertVnRoleAliases() {
        traceSaving("vnrole aliases")
        val vnClassidCol = names.column("pbroles_vnroles.vnclassid")
        val vnRoleidCol = names.column("pbroles_vnroles.vnroleid")
        val vnRoletypeidCol = names.column("pbroles_vnroles.vnroletypeid")
        val vnClassCol = names.column("pbroles_vnroles.vnclass")
        val vnRoleCol = names.column("pbroles_vnroles.vnrole")
        update(
            RoleToVn.SET,
            File(outDir, names.updateFile("pbroles_vnroles")),
            header,
            names.table("pbroles_vnroles"),
            vnRoleResolver,
            { resolved ->
                if (resolved == null)
                    "$vnClassidCol=NULL,$vnRoleidCol=NULL,$vnRoletypeidCol=NULL"
                else
                    "$vnClassidCol=${nullableInt(resolved.first)},$vnRoleidCol=${nullableInt(resolved.second)},$vnRoletypeidCol=${nullableInt(resolved.third)}"
            },
            { resolving -> "$vnClassCol='${escape(resolving.first)}' AND $vnRoleCol='${escape(resolving.second)}'" })
        traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertFnFeAliases() {
        traceSaving("fnrole aliases")
        val fnframeidCol = names.column("pbroles_fnfes.fnframeid")
        val fnfeidCol = names.column("pbroles_fnfes.fnfeid")
        val fnfetypeidCol = names.column("pbroles_fnfes.fnfetypeid")
        val fnframeCol = names.column("pbroles_fnfes.fnframe")
        val fnfeCol = names.column("pbroles_fnfes.fnfe")
        update(
            RoleToVn.SET,
            File(outDir, names.updateFile("pbroles_fnfes")),
            header,
            names.table("pbroles_fnfes"),
            fnFeResolver,
            { resolved ->
                if (resolved == null)
                    "$fnframeidCol=NULL,$fnfeidCol=NULL,$fnfetypeidCol=NULL"
                else
                    "$fnframeidCol=${nullableInt(resolved.first)},$fnfeidCol=${nullableInt(resolved.second)},$fnfetypeidCol=${nullableInt(resolved.third)}"
            },
            { resolving -> "$fnframeCol='${escape(resolving.first)}' AND $fnfeCol='${escape(resolving.second)}'" })
        traceDone()
    }
}
