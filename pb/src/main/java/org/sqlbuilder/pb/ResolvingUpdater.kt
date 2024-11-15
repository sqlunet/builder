package org.sqlbuilder.pb

import org.sqlbuilder.common.Progress
import org.sqlbuilder.common.Update
import org.sqlbuilder.common.Utils
import org.sqlbuilder.pb.foreign.FnAlias
import org.sqlbuilder.pb.foreign.VnAlias
import org.sqlbuilder.pb.foreign.VnRoleAlias
import org.sqlbuilder.pb.objects.Word
import org.sqlbuilder2.ser.Pair
import org.sqlbuilder2.ser.Triplet
import java.io.File
import java.io.FileNotFoundException
import java.util.Properties
import java.util.function.Function

class ResolvingUpdater(conf: Properties) : ResolvingInserter(conf) {
    init {
        // output
        this.outDir = File(conf.getProperty("pm_outdir_updated", "sql/data_updated"))
        if (!this.outDir.exists()) {
            this.outDir.mkdirs()
        }
    }

    @Throws(FileNotFoundException::class)
    override fun insert() {
        Word.COLLECTOR.open().use { ignored30 ->
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
            { resolved -> String.format("%s=%s", wordidCol, Utils.nullableInt(resolved)) },
            { resolving -> String.format("%s='%s'", wordCol, Utils.escape(resolving)) })
        Progress.traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertFnAliases() {
        Progress.tracePending("set", "fnalias")
        val fnframeidCol = names.column("pbrolesets_fnframes.fnframeid")
        val vnclassCol = names.column("pbrolesets_fnframes.fnframe")
        Update.update(
            FnAlias.SET,
            File(outDir, names.updateFile("pbrolesets_fnframes")),
            header,
            names.table("pbrolesets_fnframes"),
            fnFrameResolver,
            { resolved: Int? -> String.format("%s=%s", fnframeidCol, Utils.nullableInt(resolved)) },
            { resolving: String -> String.format("%s='%s'", vnclassCol, Utils.escape(resolving)) })
        Progress.traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertVnAliases() {
        Progress.tracePending("set", "vnalias")
        val vnclassidCol = names.column("pbrolesets_vnclasses.vnclassid")
        val vnclassCol = names.column("pbrolesets_vnclasses.vnclass")
        Update.update(
            VnAlias.SET,
            File(outDir, names.updateFile("pbrolesets_vnclasses")),
            header,
            names.table("pbrolesets_vnclasses"),
            vnClassResolver,
            { resolved -> String.format("%s=%s", vnclassidCol, Utils.nullableInt(resolved)) },
            { resolving -> String.format("%s='%s'", vnclassCol, Utils.escape(resolving)) })
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
            VnRoleAlias.SET,
            File(outDir, names.updateFile("pbroles_vnroles")),
            header!!,
            names.table("pbroles_vnroles")!!,
            vnClassRoleResolver,
            { resolved ->
                if (resolved == null)
                    String.format("%s=NULL,%s=NULL,%s=NULL", vnclassidCol, vnroleidCol, vnroletypeidCol)
                else
                    String.format("%s=%s,%s=%s,%s=%s",
                    vnclassidCol,
                    Utils.nullableInt(resolved.first),
                    vnroleidCol,
                    Utils.nullableInt(resolved.second),
                    vnroletypeidCol,
                    Utils.nullableInt(resolved.third)
                )
            },
            { resolving ->
                String.format("%s='%s' AND %s='%s'",
                    vnclassCol,
                    Utils.escape(resolving!!.first),
                    vnroleCol,
                    Utils.escape(resolving.second)
                )
            })
        Progress.traceDone()
    }
}
