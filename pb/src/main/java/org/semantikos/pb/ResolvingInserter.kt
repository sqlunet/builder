package org.semantikos.pb

import org.semantikos.common.Insert.resolveAndInsert
import org.semantikos.common.Progress.traceDone
import org.semantikos.common.Progress.traceSaving
import org.semantikos.common.Utils.nullable
import org.semantikos.pb.foreign.RoleSetToFn
import org.semantikos.pb.foreign.RoleSetToVn
import org.semantikos.pb.foreign.RoleToFn
import org.semantikos.pb.foreign.RoleToVn
import org.semantikos.pb.objects.Word
import java.io.File
import java.io.FileNotFoundException
import java.util.*

open class ResolvingInserter(conf: Properties) : Inserter(conf) {

    protected val wordSerFile: String

    protected val vnClassSerFile: String

    protected val vnClassRoleSerFile: String

    protected val fnFrameSerFile: String

    protected val fnFrameFeSerFile: String

    protected val wordResolver: WordResolver

    protected val vnClassResolver: VnClassResolver

    protected val vnRoleResolver: VnRoleResolver

    protected val fnFrameResolver: FnFrameResolver

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
        traceSaving("word")
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
        traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertFnFrameAliases() {
        traceSaving("fnframe aliases")
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
        traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertVnClassAliases() {
        traceSaving("vnclass aliases")
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
        traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertVnRoleAliases() {
        traceSaving("vnrole aliases")
        resolveAndInsert(
            RoleToVn.SET,
            RoleToVn.COMPARATOR,
            File(outDir, names.file("pbroles_vnroles")),
            names.table("pbroles_vnroles"),
            names.columns("pbroles_vnroles"),
            header,
            vnRoleResolver,
            RoleToVn.RESOLVE_RESULT_STRINGIFIER,
            names.column("pbroles_vnroles.vnroleid"),
            names.column("pbroles_vnroles.vnclassid"),
            names.column("pbroles_vnroles.vnroletypeid")
        )
        traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertFnFeAliases() {
        traceSaving("fnfe aliases")
        resolveAndInsert(
            RoleToFn.SET,
            RoleToFn.COMPARATOR,
            File(outDir, names.file("pbroles_fnfes")),
            names.table("pbroles_fnfes"),
            names.columns("pbroles_fnfes"),
            header,
            fnFeResolver,
            RoleToFn.RESOLVE_RESULT_STRINGIFIER,
            names.column("pbroles_fnfes.fnfeid"),
            names.column("pbroles_fnfes.fnframeid"),
            names.column("pbroles_fnfes.fnfetypeid")
        )
        traceDone()
    }
}
