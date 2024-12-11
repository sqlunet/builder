package org.sqlbuilder.su

import org.sigma.core.KB
import org.sqlbuilder.common.Progress.trace
import java.io.File
import java.io.Serializable

class Kb(dirName: String?) : KB("SUMO", dirName), Serializable {

    lateinit var filenames: Array<String>
        private set

    fun make(full: Boolean) : Int {
        return make(getFiles(kbDir, full))
    }

    fun make(files: Array<String>?) : Int {
        filenames = files ?: getFiles(kbDir, true)
        val filePaths: Array<String> = Array(filenames.size) {
            kbDir + File.separatorChar + filenames[it]
        }
        makeKB(this, filePaths)
        return filenames.size
    }

    fun makeClausalForms() {
        var count: Long = 0
        for (fs in formulaIndex.values) {
            for (f in fs) {
                f.getClausalForms()
                if (!silent) {
                    if ((count++ % 100L) == 1L) {
                        println()
                    }
                    print('.')
                }
            }
        }
    }

    companion object {

        private val CORE_FILES = arrayOf("Merge.kif", "Mid-level-ontology.kif", "english_format.kif")

        private val silent = System.getProperties().containsKey("SILENT")

        private fun makeKB(kb: KB, filePaths: Array<String>) {
            filePaths
                .withIndex()
                .forEach { (i, f) ->
                    //System.err.println(basename(f))
                    kb.addConstituent(f)
                    trace(i.toLong())
                }
        }

        private fun getFiles(dirName: String, full: Boolean): Array<String> {
            if (full) {
                val list: MutableList<String> = ArrayList(listOf<String>(*CORE_FILES))
                getKifs(dirName)
                    ?.filterNot { list.contains(it) }
                    ?.forEach {
                        list.add(it)
                    }
                return list.toTypedArray()
            }
            return CORE_FILES
        }

        private fun getKifs(dirName: String): Array<String>? {
            val file = File(dirName)
            if (file.exists() && file.isDirectory()) {
                return file.list { dir: File, name: String -> name.endsWith(".kif") }
            }
            return arrayOf<String>()
        }
    }
}
