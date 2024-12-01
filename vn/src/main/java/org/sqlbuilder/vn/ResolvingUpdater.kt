package org.sqlbuilder.vn

import org.sqlbuilder.common.Progress.traceDone
import org.sqlbuilder.common.Progress.tracePending
import org.sqlbuilder.common.Update.update
import org.sqlbuilder.common.Utils.escape
import org.sqlbuilder.common.Utils.nullableInt
import org.sqlbuilder.vn.objects.Sense
import org.sqlbuilder.vn.objects.Word
import java.io.File
import java.io.FileNotFoundException
import java.util.*
import java.util.AbstractMap.SimpleEntry

class ResolvingUpdater(conf: Properties) : ResolvingInserter(conf) {
    init {
        // output
        outDir = File(conf.getProperty("vn_outdir_updated", "sql/data_updated"))
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
    }

    @Throws(FileNotFoundException::class)
    override fun insert() {
        Word.COLLECTOR.open().use { 
            insertWords()
            insertMemberSenses()
        }
    }

    @Throws(FileNotFoundException::class)
    override fun insertWords() {
        tracePending("collector", "word")
        val wordidCol = names.column("words.wordid")
        val wordCol = names.column("words.word")
        update<Word, String, Int>(
            Word.COLLECTOR, File(outDir, names.updateFile("words")), header, names.table("words"),
            wordResolver,
            { resolved: Int? -> "$wordidCol=${nullableInt(resolved)}" },
            { resolving: String -> "$wordCol='${escape(resolving)}'" })
        traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertMemberSenses() {
        tracePending("set", "members senses")
        val wordidCol = names.column("members_senses.wordid")
        val synsetidCol = names.column("members_senses.synsetid")
        val sensekeyCol = names.column("members_senses.sensekey")
        update<Sense, String, SimpleEntry<Int, Int>>(
            Sense.SET, File(outDir, names.updateFile("members_senses")), header, names.table("members_senses"),
            sensekeyResolver,
            { resolved: SimpleEntry<Int, Int>? -> if (resolved == null) "$wordidCol=NULL,$synsetidCol=NULL" else "$wordidCol=${nullableInt(resolved.key)},$synsetidCol=${nullableInt(resolved.value)}" },
            { resolving: String? -> "$sensekeyCol='${escape(resolving!!)}'" })
        traceDone()
    }
}
