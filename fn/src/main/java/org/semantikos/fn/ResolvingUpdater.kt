package org.semantikos.fn

import org.semantikos.common.Progress.traceDone
import org.semantikos.common.Progress.traceSaving
import org.semantikos.common.Update.update
import org.semantikos.common.Utils.escape
import org.semantikos.common.Utils.nullableInt
import org.semantikos.fn.objects.Word
import java.io.File
import java.io.FileNotFoundException
import java.util.*

class ResolvingUpdater(conf: Properties) : ResolvingInserter(conf) {

    init {
        // output
        outDir = File(conf.getProperty("fn_outdir_updated", "sql/data_updated"))
        if (!outDir.exists()) {
            outDir.mkdirs()
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
        traceSaving("word")
        val wordidCol = names.column("words.wordid")
        val wordCol = names.column("words.word")
        update(
            Word.COLLECTOR, File(outDir, names.updateFile("words")), header, names.table("words"),
            resolver,
            { resolved -> "$wordidCol=${nullableInt(resolved)}" },
            { resolving -> "$wordCol='${escape(resolving)}'" }
        )
        traceDone()
    }
}
