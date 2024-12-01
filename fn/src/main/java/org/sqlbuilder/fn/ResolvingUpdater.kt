package org.sqlbuilder.fn

import org.sqlbuilder.common.Progress.traceDone
import org.sqlbuilder.common.Progress.tracePending
import org.sqlbuilder.common.Update.update
import org.sqlbuilder.common.Utils.escape
import org.sqlbuilder.common.Utils.nullableInt
import org.sqlbuilder.fn.objects.Word
import java.io.File
import java.io.FileNotFoundException
import java.util.*

class ResolvingUpdater(conf: Properties) : ResolvingInserter(conf) {

    init {
        // output
        this.outDir = File(conf.getProperty("fn_outdir_updated", "sql/data_updated"))
        if (!this.outDir.exists()) {
            this.outDir.mkdirs()
        }
    }

    @Throws(FileNotFoundException::class)
    override fun insert() {
        Word.COLLECTOR.open().use { 
            insertWords()
        }
    }

    @Throws(FileNotFoundException::class)
    override fun insertWords() {
        tracePending("collector", "word")
        val wordidCol = names.column("words.wordid")
        val wordCol = names.column("words.word")
        update<Word, String, Int>(
            Word.COLLECTOR, File(outDir, names.updateFile("words")), header, names.table("words"),
            resolver,
            { resolved -> "$wordidCol=${nullableInt(resolved)}" },
            { resolving -> "$wordCol='${escape(resolving)}'" }
        )
        traceDone()
    }
}
