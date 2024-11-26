package org.sqlbuilder.pb

import org.sqlbuilder.common.Insert
import org.sqlbuilder.common.Progress
import org.sqlbuilder.common.Utils
import org.sqlbuilder.pb.foreign.FnAlias
import org.sqlbuilder.pb.foreign.VnAlias
import org.sqlbuilder.pb.foreign.VnRoleAlias
import org.sqlbuilder.pb.objects.Word
import java.io.File
import java.io.FileNotFoundException
import java.util.*

open class ResolvingInserter(conf: Properties) : Inserter(conf) {

    protected val wordSerFile: String

    protected val vnClassSerFile: String

    protected val vnClassRoleSerFile: String

    protected val fnFrameSerFile: String

    @JvmField
    protected val wordResolver: WordResolver

    @JvmField
    protected val vnClassResolver: VnClassResolver

    @JvmField
    protected val vnClassRoleResolver: VnClassRoleResolver

    @JvmField
    protected val fnFrameResolver: FnFrameResolver

    init {
        // header
        this.header += "\n-- " + conf.getProperty("wn_resolve_against")
        this.header += "\n-- " + conf.getProperty("vn_resolve_against")
        this.header += "\n-- " + conf.getProperty("fn_resolve_against")

        // output
        this.outDir = File(conf.getProperty("pb_outdir_resolved", "sql/data_resolved"))
        if (!this.outDir.exists()) {
            this.outDir.mkdirs()
        }

        // resolve
        this.wordSerFile = conf.getProperty("word_nids")
        this.vnClassSerFile = conf.getProperty("vnclass_nids")
        this.vnClassRoleSerFile = conf.getProperty("vnrole_nids")
        this.fnFrameSerFile = conf.getProperty("fnframe_nids")

        this.wordResolver = WordResolver(wordSerFile)
        this.vnClassResolver = VnClassResolver(vnClassSerFile)
        this.vnClassRoleResolver = VnClassRoleResolver(vnClassRoleSerFile)
        this.fnFrameResolver = FnFrameResolver(this.fnFrameSerFile)
    }

    @Throws(FileNotFoundException::class)
    override fun insertWords() {
        Progress.tracePending("collector", "word")
        Insert.resolveAndInsert(
            Word.COLLECTOR,
            Word.COLLECTOR,
            File(outDir, names.file("words")),
            names.table("words"),
            names.columns("words"),
            header,
            true,
            wordResolver,
            { Objects.toString(it) },
            names.column("words.wordid")
        )
        Progress.traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertFnAliases() {
        Progress.tracePending("set", "fnalias")
        Insert.resolveAndInsert(
            FnAlias.SET,
            FnAlias.COMPARATOR,
            File(outDir, names.file("pbrolesets_fnframes")),
            names.table("pbrolesets_fnframes"),
            names.columns("pbrolesets_fnframes"),
            header,
            fnFrameResolver,
            { r -> Utils.nullable(r) { Objects.toString(it) } },
            names.column("pbrolesets_fnframes.fnframeid")
        )
        Progress.traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertVnAliases() {
        Progress.tracePending("set", "vnalias")
        Insert.resolveAndInsert(
            VnAlias.SET,
            VnAlias.COMPARATOR,
            File(outDir, names.file("pbrolesets_vnclasses")),
            names.table("pbrolesets_vnclasses"),
            names.columns("pbrolesets_vnclasses"),
            header,
            vnClassResolver,
            { r -> Utils.nullable(r) { Objects.toString(it) } },
            names.column("pbrolesets_vnclasses.vnclassid")
        )
        Progress.traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertVnRoleAliases() {
        Progress.tracePending("set", "vnaliasrole")
        Insert.resolveAndInsert(
            VnRoleAlias.SET,
            VnRoleAlias.COMPARATOR,
            File(outDir, names.file("pbroles_vnroles")),
            names.table("pbroles_vnroles"),
            names.columns("pbroles_vnroles"),
            header,
            vnClassRoleResolver,
            VnRoleAlias.RESOLVE_RESULT_STRINGIFIER,
            names.column("pbroles_vnroles.vnroleid"),
            names.column("pbroles_vnroles.vnclassid"),
            names.column("pbroles_vnroles.vnroletypeid")
        )
        Progress.traceDone()
    }
}
