package org.semantikos.fn

import org.semantikos.common.Insert.resolveAndInsert
import org.semantikos.common.Progress.traceDone
import org.semantikos.common.Progress.traceSaving
import org.semantikos.common.Utils.nullable
import org.semantikos.fn.objects.Word
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
        resolveAndInsert(
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
