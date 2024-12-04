package org.sqlbuilder.pb

import org.sqlbuilder.common.Insert
import org.sqlbuilder.common.Insert.resolveAndInsert
import org.sqlbuilder.common.Progress
import org.sqlbuilder.common.Utils.nullable
import org.sqlbuilder.pb.foreign.RoleSetToFn
import org.sqlbuilder.pb.foreign.RoleSetToVn
import org.sqlbuilder.pb.foreign.RoleToFn
import org.sqlbuilder.pb.foreign.RoleToVn
import org.sqlbuilder.pb.objects.Word
import java.io.File
import java.io.FileNotFoundException
import java.util.*

open class ResolvingInserter(conf: Properties) : Inserter(conf) {

    protected val wordSerFile: String

    protected val vnClassSerFile: String

    protected val vnClassRoleSerFile: String

    protected val fnFrameSerFile: String

    protected val fnFrameFeSerFile: String

    @JvmField
    protected val wordResolver: WordResolver

    @JvmField
    protected val vnClassResolver: VnClassResolver

    @JvmField
    protected val vnRoleResolver: VnRoleResolver

    @JvmField
    protected val fnFrameResolver: FnFrameResolver

    @JvmField
    protected val fnFeResolver: FnFeResolver

    init {
        // header
        header += "\n-- " + conf.getProperty("wn_resolve_against")
        header += "\n-- " + conf.getProperty("vn_resolve_against")
        header += "\n-- " + conf.getProperty("fn_resolve_against")

        // output
        outDir = File(conf.getProperty("pb_outdir_resolved", "sql/data_resolved"))
        if (!outDir.exists()) {
            outDir.mkdirs()
        }

        // resolve
        wordSerFile = conf.getProperty("word_nids")
        vnClassSerFile = conf.getProperty("vnclass_nids")
        vnClassRoleSerFile = conf.getProperty("vnrole_nids")
        fnFrameSerFile = conf.getProperty("fnframe_nids")
        fnFrameFeSerFile = conf.getProperty("fnfe_nids")

        wordResolver = WordResolver(wordSerFile)
        vnClassResolver = VnClassResolver(vnClassSerFile)
        vnRoleResolver = VnRoleResolver(vnClassRoleSerFile)
        fnFrameResolver = FnFrameResolver(fnFrameSerFile)
        fnFeResolver = FnFeResolver(fnFrameFeSerFile)
    }

    @Throws(FileNotFoundException::class)
    override fun insertWords() {
        Progress.tracePending("collector", "word")
        resolveAndInsert(
            Word.COLLECTOR,
            Word.COLLECTOR,
            File(outDir, names.file("words")),
            names.table("words"),
            names.columns("words"),
            header,
            true,
            wordResolver,
            { nullable(it) { it.toString() } },
            names.column("words.wordid")
        )
        Progress.traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertFnFrameAliases() {
        Progress.tracePending("set", "fnalias")
        resolveAndInsert(
            RoleSetToFn.SET,
            RoleSetToFn.COMPARATOR,
            File(outDir, names.file("pbrolesets_fnframes")),
            names.table("pbrolesets_fnframes"),
            names.columns("pbrolesets_fnframes"),
            header,
            fnFrameResolver,
            { nullable(it) { it.toString() } },
            names.column("pbrolesets_fnframes.fnframeid")
        )
        Progress.traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertVnClassAliases() {
        Progress.tracePending("set", "vnalias")
        resolveAndInsert(
            RoleSetToVn.SET,
            RoleSetToVn.COMPARATOR,
            File(outDir, names.file("pbrolesets_vnclasses")),
            names.table("pbrolesets_vnclasses"),
            names.columns("pbrolesets_vnclasses"),
            header,
            vnClassResolver,
            { nullable(it) { it.toString() } },
            names.column("pbrolesets_vnclasses.vnclassid")
        )
        Progress.traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertVnRoleAliases() {
        Progress.tracePending("set", "vnaliasrole")
        resolveAndInsert(
            RoleToVn.SET,
            RoleToVn.COMPARATOR,
            File(outDir, names.file("pbroles_vnroles")),
            names.table("pbroles_vnroles"),
            names.columns("pbroles_vnroles"),
            header,
            { vnRoleResolver.apply(it) },
            RoleToVn.RESOLVE_RESULT_STRINGIFIER,
            names.column("pbroles_vnroles.vnroleid"),
            names.column("pbroles_vnroles.vnclassid"),
            names.column("pbroles_vnroles.vnroletypeid")
        )
        Progress.traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertFnFeAliases() {
        Progress.tracePending("set", "fnaliasrole")
        resolveAndInsert(
            RoleToFn.SET,
            RoleToFn.COMPARATOR,
            File(outDir, names.file("pbroles_fnfes")),
            names.table("pbroles_fnfes"),
            names.columns("pbroles_fnfes"),
            header,
            { fnFeResolver.apply(it) },
            RoleToFn.RESOLVE_RESULT_STRINGIFIER,
            names.column("pbroles_fnfes.fnfeid"),
            names.column("pbroles_fnfes.fnframeid"),
            names.column("pbroles_fnfes.fnfetypeid")
        )
        Progress.traceDone()
    }
}
