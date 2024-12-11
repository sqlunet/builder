package org.sqlbuilder.fn.collectors

import org.sqlbuilder.common.Processor
import org.sqlbuilder.common.Progress.trace
import org.sqlbuilder.common.Progress.traceHeader
import org.sqlbuilder.common.Progress.traceTailer
import java.io.File
import java.io.FilenameFilter
import java.util.*

abstract class FnCollector(protected val fnDir: String, props: Properties, tag: String) : Processor(tag) {

    protected val frameNetHome: String = props.getProperty("fn_home", System.getenv()["FNHOME"])

    protected lateinit var filename: String

    override fun run() {
        val folderName = frameNetHome + File.separatorChar + fnDir
        val folder = File(folderName)
        val filter = FilenameFilter { dir: File, filename2: String -> filename2.endsWith(".xml") }

        traceHeader("framenet", "reading files")
        var fileCount = 0
        val files = folder.listFiles(filter)
        if (files == null) {
            throw RuntimeException("Dir:$frameNetHome is empty")
        }
        files
            .asSequence()
            .sortedWith(Comparator.comparing<File, String> { it.name })
            .forEach {
                filename = it.getName()
                try {
                    processFrameNetFile(it.absolutePath)
                    fileCount++
                } catch (e: Exception) {
                    throw RuntimeException("File:$filename", e)
                }
                trace(fileCount.toLong())
            }
        traceTailer(fileCount.toLong())
    }

    protected abstract fun processFrameNetFile(fileName: String)
}
