package org.sqlbuilder.pb

import org.sqlbuilder.common.Progress
import org.sqlbuilder.common.Update
import org.sqlbuilder.common.Utils.escape
import org.sqlbuilder.common.Utils.nullableInt
import org.sqlbuilder.pb.foreign.RoleSetToFn
import org.sqlbuilder.pb.foreign.RoleSetToVn
import org.sqlbuilder.pb.foreign.RoleToVn
import org.sqlbuilder.pb.objects.Word
import org.sqlbuilder2.ser.Pair
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
            Word.COLLECTOR,
            File(outDir, names.updateFile("words")),
            header,
            names.table("words"),
            wordResolver,
            { resolved -> "$wordidCol=${nullableInt(resolved)}" },
            { resolving -> "$wordCol='${escape(resolving)}'" })
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
            { resolved -> "$vnclassidCol=${nullableInt(resolved)}" },
            { resolving -> "$vnclassCol='${escape(resolving)}'" })
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
            { resolved -> "$fnframeidCol=${nullableInt(resolved)}" },
            { resolving -> "$vnclassCol='${escape(resolving)}'" })
        Progress.traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertVnRoleAliases() {
        Progress.tracePending("set", "vnaliasrole")
        val vnClassidCol = names.column("pbroles_vnroles.vnclassid")
        val vnRoleidCol = names.column("pbroles_vnroles.vnroleid")
        val vnRoletypeidCol = names.column("pbroles_vnroles.vnroletypeid")
        val vnClassCol = names.column("pbroles_vnroles.vnclass")
        val vnRoleCol = names.column("pbroles_vnroles.vnrole")
        Update.update(
            RoleToVn.SET,
            File(outDir, names.updateFile("pbroles_vnroles")),
            header,
            names.table("pbroles_vnroles"),
            { p: Pair<String?, String?> -> vnClassRoleResolver.apply(p) },
            { resolved ->
                if (resolved == null)
                    "$vnClassidCol=NULL,$vnRoleidCol=NULL,$vnRoletypeidCol=NULL"
                else
                    "$vnClassidCol=${nullableInt(resolved.first)},$vnRoleidCol=${nullableInt(resolved.second)},$vnRoletypeidCol=${nullableInt(resolved.third)}"
            },
            { resolving ->
                "$vnClassCol='${escape(resolving.first!!)}' AND $vnRoleCol='${escape(resolving.second!!)}'"
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
            names.table("pbroles_fnfes"),
            { p: Pair<String?, String?> -> fnFrameFeResolver.apply(p) },
            { resolved ->
                if (resolved == null)
                    "$fnframeidCol=NULL,$fnfeidCol=NULL,$fnfetypeidCol=NULL"
                else
                    "$fnframeidCol=${nullableInt(resolved.first)},$fnfeidCol=${nullableInt(resolved.second)},$fnfetypeidCol=${nullableInt(resolved.third)}"
            },
            { resolving ->
                "$fnframeCol='${escape(resolving.first!!)}' AND $fnfeCol='${escape(resolving.second!!)}'"
            })
        Progress.traceDone()
    }
}
