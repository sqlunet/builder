package org.semantikos.vn

import org.semantikos.common.Progress.traceDone
import org.semantikos.common.Progress.traceSaving
import org.semantikos.common.Update.update
import org.semantikos.common.Utils.escape
import org.semantikos.common.Utils.nullable
import org.semantikos.common.Utils.nullableInt
import org.semantikos.vn.objects.Sense
import org.semantikos.vn.objects.Word
import java.io.File
import java.io.FileNotFoundException
import java.util.*

class ResolvingUpdater(conf: Properties) : ResolvingInserter(conf) {
    init {
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
        traceSaving("word")
        val wordidCol = names.column("words.wordid")
        val wordCol = names.column("words.word")
        update(
            Word.COLLECTOR,
            File(outDir, names.updateFile("words")),
            header,
            names.table("words"),
            wordResolver,
            { resolved -> "$wordidCol=${nullableInt(resolved)}" },
            { resolving -> "$wordCol='${escape(resolving)}'" })
        traceDone()
    }

    @Throws(FileNotFoundException::class)
    override fun insertMemberSenses() {
        traceSaving("members senses")
        val wordidCol = names.column("members_senses.wordid")
        val synsetidCol = names.column("members_senses.synsetid")
        val sensekeyCol = names.column("members_senses.sensekey")
        update(
            Sense.SET,
            File(outDir, names.updateFile("members_senses")),
            header,
            names.table("members_senses"),
            sensekeyResolver,
            { resolved -> if (resolved == null) "$wordidCol=NULL,$synsetidCol=NULL" else "$wordidCol=${nullableInt(resolved.first)},$synsetidCol=${nullableInt(resolved.second)}" },
            { resolving -> "$sensekeyCol='${nullable(resolving) { escape(it) }}'" }
        )
        traceDone()
    }
}
