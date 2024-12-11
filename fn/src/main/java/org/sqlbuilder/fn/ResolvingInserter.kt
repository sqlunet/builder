package org.sqlbuilder.fn

import org.sqlbuilder.common.Insert.resolveAndInsert
import org.sqlbuilder.common.Progress.traceDone
import org.sqlbuilder.common.Progress.traceSaving
import org.sqlbuilder.common.Utils.nullable
import org.sqlbuilder.fn.objects.Word
import java.io.File
import java.io.FileNotFoundException
import java.util.*

open class ResolvingInserter(conf: Properties) : Inserter(conf) {

    protected val serFile: String = conf.getProperty("word_nids")

    protected val resolver: FnWordResolver = FnWordResolver(serFile)

    init {
        // header
        header += "\n-- " + conf.getProperty("wn_resolve_against")

        // output
        outDir = File(conf.getProperty("fn_outdir_resolved", "sql/data_resolved"))
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
    }

    @Throws(FileNotFoundException::class)
    override fun insertWords() {
        traceSaving("word")
        resolveAndInsert<Word, String, Int>(
            Word.COLLECTOR,
            Word.COLLECTOR,
            File(outDir, names.file("words")),
            names.table("words"),
            names.columns("words"),
            header, true,
            resolver,
            { nullable(it) { it.toString() } },
            names.column("words.wordid")
        )
        traceDone()
    }
}
