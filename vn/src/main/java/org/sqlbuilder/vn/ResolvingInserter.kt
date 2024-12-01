package org.sqlbuilder.vn

import org.sqlbuilder.common.Insert.resolveAndInsert
import org.sqlbuilder.common.Progress.traceDone
import org.sqlbuilder.common.Progress.tracePending
import org.sqlbuilder.common.Utils.nullable
import org.sqlbuilder.vn.joins.Member_Sense
import org.sqlbuilder.vn.objects.Word
import java.io.File
import java.io.FileNotFoundException
import java.util.*
import java.util.AbstractMap.SimpleEntry
import java.util.function.Function

open class ResolvingInserter(conf: Properties) : Inserter(conf) {

    protected val wordSerFile: String

    protected val sensekeySerFile: String

    @JvmField
    protected val wordResolver: VnWordResolver

    @JvmField
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
        tracePending("collector", "word")
        resolveAndInsert<Word, String, Int>(
            Word.COLLECTOR, Word.COLLECTOR, File(outDir, names.file("words")), names.table("words"), names.columns("words"), header, true,
            wordResolver,
            { nullable(it) { it.toString() } },
            names.column("words.wordid")
        )
        traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertMemberSenses() {
        tracePending("set", "member sense")
        resolveAndInsert<Member_Sense, String, SimpleEntry<Int, Int>?>(
            Member_Sense.SET, Member_Sense.COMPARATOR, File(outDir, names.file("members_senses")), names.table("members_senses"), names.columns("members_senses"), header,
            sensekeyResolver,
            Member_Sense.RESOLVE_RESULT_STRINGIFIER,
            names.column("members_senses.wordid"),
            names.column("members_senses.synsetid")
        )
        traceDone()
    }
}
