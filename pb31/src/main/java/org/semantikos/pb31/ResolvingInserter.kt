package org.semantikos.pb31

import org.semantikos.common.Insert.resolveAndInsert
import org.semantikos.common.Progress.traceDone
import org.semantikos.common.Progress.traceSaving
import org.semantikos.common.Utils.nullable
import org.semantikos.pb31.foreign.RoleSetToFn
import org.semantikos.pb31.foreign.RoleSetToVn
import org.semantikos.pb31.foreign.RoleToVn
import org.semantikos.pb31.objects.Word
import java.io.File
import java.io.FileNotFoundException
import java.util.*

open class ResolvingInserter(conf: Properties) : Inserter(conf) {

    protected val wordSerFile: String

    protected val vnClassSerFile: String

    protected val vnClassRoleSerFile: String

    protected val fnFrameSerFile: String

    protected val wordResolver: WordResolver

    protected val vnClassResolver: VnClassResolver

    protected val vnClassRoleResolver: VnClassRoleResolver

    protected val fnFrameResolver: FnFrameResolver

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

        wordResolver = WordResolver(wordSerFile)
        vnClassResolver = VnClassResolver(vnClassSerFile)
        vnClassRoleResolver = VnClassRoleResolver(vnClassRoleSerFile)
        fnFrameResolver = FnFrameResolver(fnFrameSerFile)
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
    override fun insertFnAliases() {
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
    override fun insertVnAliases() {
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
            vnClassRoleResolver,
            RoleToVn.RESOLVE_RESULT_STRINGIFIER,
            names.column("pbroles_vnroles.vnroleid"),
            names.column("pbroles_vnroles.vnclassid"),
            names.column("pbroles_vnroles.vnroletypeid")
        )
        traceDone()
    }
}
