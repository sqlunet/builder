package org.sqlbuilder.fn.collectors

import org.sqlbuilder.common.Processor
import org.sqlbuilder.common.Progress.trace
import org.sqlbuilder.common.Progress.traceHeader
import org.sqlbuilder.common.Progress.traceTailer
import java.io.File
import java.util.*

abstract class FnCollector1(protected val filename: String, props: Properties, tag: String) : Processor(tag) {

    protected val fnHome: String = props.getProperty("fn_home", System.getenv()["FNHOME"])

    override fun run() {
        traceHeader("framenet file", filename)
        val file = File(fnHome + File.separatorChar + filename)
        processFrameNetFile(file.absolutePath)
        trace(1)
        traceTailer(1)
    }

    protected abstract fun processFrameNetFile(fileName: String)
}
