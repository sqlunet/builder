package org.sqlbuilder.su

import org.sqlbuilder.common.Progress.resetGranularity
import org.sqlbuilder.common.Progress.setGranularity
import org.sqlbuilder.common.Progress.traceHeader
import org.sqlbuilder.common.Progress.traceTailer

open class KBLoader {

    open fun load() {
        kb = loadKb()
    }

    companion object {

        var kb: Kb? = null

        val path: String
            get() {
                var kbPath = System.getProperty("sumopath")
                if (kbPath == null) {
                    kbPath = System.getenv("SUMOHOME")
                }
                checkNotNull(kbPath)
                return kbPath
            }

        val ALL_FILES: Array<String>? = null

        val CORE_FILES: Array<String> = arrayOf("Merge.kif", "Mid-level-ontology.kif")

        val SAMPLE_FILES: Array<String> = arrayOf("Merge.kif", "Mid-level-ontology.kif", "Communications.kif")

        val scope: Array<String>?
            get() {
                val scope = System.getProperties().getProperty("SCOPE", "all")
                return when (scope) {
                    "all"     -> ALL_FILES
                    "core"    -> CORE_FILES
                    "samples" -> SAMPLE_FILES
                    else      -> scope.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                }
            }

        fun loadKb(files: Array<String>? = scope): Kb {
            val kbPath: String = path
            val kb = Kb(kbPath)
            traceHeader("sumo", path)
            setGranularity(1L)
            val n = kb.make(files)
            resetGranularity()
            traceTailer(n.toLong())
            return kb
        }
    }
}
