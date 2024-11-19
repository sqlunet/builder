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
            insertFnAliases()
            insertVnAliases()
            insertVnRoleAliases()
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
    override fun insertFnAliases() {
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
    override fun insertVnAliases() {
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
    override fun insertVnRoleAliases() {
        Progress.tracePending("set", "vnaliasrole")
        val vnclassidCol = names.column("pbroles_vnroles.vnclassid")
        val vnroleidCol = names.column("pbroles_vnroles.vnroleid")
        val vnroletypeidCol = names.column("pbroles_vnroles.vnroletypeid")
        val vnclassCol = names.column("pbroles_vnroles.vnclass")
        val vnroleCol = names.column("pbroles_vnroles.vntheta")
        Update.update(
            RoleToVn.SET,
            File(outDir, names.updateFile("pbroles_vnroles")),
            header!!,
            names.table("pbroles_vnroles")!!,
            vnClassRoleResolver,
            { resolved ->
                if (resolved == null)
                    "$vnclassidCol=NULL,$vnroleidCol=NULL,$vnroletypeidCol=NULL"
                else
                    "$vnclassidCol=${Utils.nullableInt(resolved.first)},$vnroleidCol=${Utils.nullableInt(resolved.second)},$vnroletypeidCol=${Utils.nullableInt(resolved.third)}"
            },
            { resolving ->
                "$vnclassCol='${Utils.escape(resolving!!.first)}' AND $vnroleCol='${Utils.escape(resolving.second)}'"
            })
        Progress.traceDone()
    }
}
