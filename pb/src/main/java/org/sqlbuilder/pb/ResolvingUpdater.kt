package org.sqlbuilder.pb

import org.sqlbuilder.common.Progress
import org.sqlbuilder.common.Update
import org.sqlbuilder.common.Utils
import org.sqlbuilder.pb.foreign.RoleSetToFn
import org.sqlbuilder.pb.foreign.RoleSetToVn
import org.sqlbuilder.pb.foreign.RoleToVn
import org.sqlbuilder.pb.objects.Word
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
        Progress.tracePending("collector", "word")
        val wordidCol = names.column("words.wordid")
        val wordCol = names.column("words.word")
        Update.update(
            Word.COLLECTOR.keys,
            File(outDir, names.updateFile("words")),
            header,
            names.table("words"),
            wordResolver,
            { resolved -> "$wordidCol=${Utils.nullableInt(resolved)}" },
            { resolving -> "%$wordCol='${Utils.escape(resolving)}'" })
        Progress.traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertVnClassAliases() {
        Progress.tracePending("set", "vnalias")
        val vnclassidCol = names.column("pbrolesets_vnclasses.vnclassid")
        val vnclassCol = names.column("pbrolesets_vnclasses.vnclass")
        Update.update(
            RoleSetToVn.SET,
            File(outDir, names.updateFile("pbrolesets_vnclasses")),
            header,
            names.table("pbrolesets_vnclasses"),
            vnClassResolver,
            { resolved -> "$vnclassidCol=${Utils.nullableInt(resolved)}" },
            { resolving -> "$vnclassCol='${Utils.escape(resolving)}'" })
        Progress.traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertFnFrameAliases() {
        Progress.tracePending("set", "fnalias")
        val fnframeidCol = names.column("pbrolesets_fnframes.fnframeid")
        val vnclassCol = names.column("pbrolesets_fnframes.fnframe")
        Update.update(
            RoleSetToFn.SET,
            File(outDir, names.updateFile("pbrolesets_fnframes")),
            header,
            names.table("pbrolesets_fnframes"),
            fnFrameResolver,
            { resolved -> "$fnframeidCol=${Utils.nullableInt(resolved)}" },
            { resolving -> "$vnclassCol='${Utils.escape(resolving)}'" })
        Progress.traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertVnRoleAliases() {
        Progress.tracePending("set", "vnaliasrole")
        val vnClassidCol = names.column("pbroles_vnroles.vnclassid")
        val vnRoleidCol = names.column("pbroles_vnroles.vnroleid")
        val vnRoletypeidCol = names.column("pbroles_vnroles.vnroletypeid")
        val vnClassCol = names.column("pbroles_vnroles.vnclass")
        val vnRoleCol = names.column("pbroles_vnroles.fnfe")
        Update.update(
            RoleToVn.SET,
            File(outDir, names.updateFile("pbroles_vnroles")),
            header,
            names.table("pbroles_vnroles")!!,
            vnClassRoleResolver,
            { resolved ->
                if (resolved == null)
                    "$vnClassidCol=NULL,$vnRoleidCol=NULL,$vnRoletypeidCol=NULL"
                else
                    "$vnClassidCol=${Utils.nullableInt(resolved.first)},$vnRoleidCol=${Utils.nullableInt(resolved.second)},$vnRoletypeidCol=${Utils.nullableInt(resolved.third)}"
            },
            { resolving ->
                "$vnClassCol='${Utils.escape(resolving!!.first)}' AND $vnRoleCol='${Utils.escape(resolving.second)}'"
            })
        Progress.traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertFnFeAliases() {
        Progress.tracePending("set", "fnaliasrole")
        val fnframeidCol = names.column("pbroles_fnfes.fnframeid")
        val fnfeidCol = names.column("pbroles_fnfes.fnfeid")
        val fnfetypeidCol = names.column("pbroles_fnfes.fnfetypeid")
        val fnframeCol = names.column("pbroles_fnfes.fnframe")
        val fnfeCol = names.column("pbroles_fnfes.fnfe")
        Update.update(
            RoleToVn.SET,
            File(outDir, names.updateFile("pbroles_fnfes")),
            header,
            names.table("pbroles_fnfes")!!,
            fnFrameFeResolver,
            { resolved ->
                if (resolved == null)
                    "$fnframeidCol=NULL,$fnfeidCol=NULL,$fnfetypeidCol=NULL"
                else
                    "$fnframeidCol=${Utils.nullableInt(resolved.first)},$fnfeidCol=${Utils.nullableInt(resolved.second)},$fnfetypeidCol=${Utils.nullableInt(resolved.third)}"
            },
            { resolving ->
                "$fnframeCol='${Utils.escape(resolving!!.first)}' AND $fnfeCol='${Utils.escape(resolving.second)}'"
            })
        Progress.traceDone()
    }
}
