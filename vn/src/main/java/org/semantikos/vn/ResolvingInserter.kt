package org.semantikos.vn

import org.semantikos.common.Insert.resolveAndInsert
import org.semantikos.common.Progress.traceDone
import org.semantikos.common.Progress.traceSaving
import org.semantikos.common.Utils.nullable
import org.semantikos.vn.joins.Member_Sense
import org.semantikos.vn.objects.Word
import java.io.File
import java.io.FileNotFoundException
import java.util.*

open class ResolvingInserter(conf: Properties) : Inserter(conf) {

    protected val wordSerFile: String

    protected val sensekeySerFile: String

    protected val wordResolver: VnWordResolver

    protected val sensekeyResolver: VnSensekeyResolver

    init {
        // header
        header += "\n-- ${conf.getProperty("wn_resolve_against")}"

        // output
        outDir = File(conf.getProperty("vn_outdir_resolved", "sql/data_resolved"))
        if (!outDir.exists()) {
            outDir.mkdirs()
        }

        // resolve
        wordSerFile = conf.getProperty("word_nids")
        sensekeySerFile = conf.getProperty("sense_nids")
        wordResolver = VnWordResolver(wordSerFile)
        sensekeyResolver = VnSensekeyResolver(sensekeySerFile)
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
    override fun insertMemberSenses() {
        traceSaving("member sense")
        resolveAndInsert(
            Member_Sense.SET,
            Member_Sense.COMPARATOR,
            File(outDir, names.file("members_senses")),
            names.table("members_senses"),
            names.columns("members_senses"),
            header,
            sensekeyResolver,
            Member_Sense.RESOLVE_RESULT_STRINGIFIER,
            names.column("members_senses.wordid"),
            names.column("members_senses.synsetid")
        )
        traceDone()
    }
}
