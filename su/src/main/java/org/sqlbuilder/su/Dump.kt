package org.sqlbuilder.su

import com.articulate.sigma.KB
import java.io.PrintStream

object Dump {

    private const val UP = "\uD83E\uDC45"

    private const val DOWN = "\uD83E\uDC47"

    fun dumpTerms(kb: KB, ps: PrintStream) {
        kb.terms.forEach {
            val doc = getDoc(kb, it)
            ps.println("$it $doc")
            dumpParents(kb, it, ps)
            dumpChildren(kb, it, ps)
        }
        ps.println(kb.terms.size)
    }

    fun dumpParents(kb: KB, term: String, ps: PrintStream) {
        kb.askWithRestriction(0, "subclass", 1, term)
            ?.withIndex()
            ?.forEach { (i, f) ->
                val formulaString = f.getArgument(2)
                ps.println("\t$UP $i $formulaString")
            }
    }

    fun dumpChildren(kb: KB, term: String, ps: PrintStream) {
        kb.askWithRestriction(0, "subclass", 2, term)
            ?.withIndex()
            ?.forEach { (i, f) ->
                val formulaString = f.getArgument(1)
                ps.println("\t$DOWN $i $formulaString")
            }
    }

    fun dumpFormulas(kb: KB, ps: PrintStream) {
        var count = 0
        kb.formulas.values
            .asSequence()
            .sorted()
            .forEach {
                ps.println(it)
                count++
            }
        ps.println(count)
    }

    fun dumpFunctions(kb: KB, ps: PrintStream) {
        var count = 0
        kb.collectFunctions()
            .asSequence()
            .sorted()
            .forEach {
                ps.println(it)
                count++
            }
        ps.println(count)
    }

    fun dumpRelations(kb: KB, ps: PrintStream) {
        var count = 0
        kb.collectRelations()
            .asSequence()
            .sorted()
            .forEach {
                ps.println(it)
                count++
            }
        ps.println(count)
    }

    fun dumpPredicates(kb: KB, ps: PrintStream) {
        var count = 0
        kb.collectPredicates()
            .asSequence()
            .sorted()
            .forEach {
                ps.println(it)
                count++
            }
        ps.println(count)
    }

    private fun getDoc(kb: KB, term: String): String {
        return kb.askWithRestriction(0, "documentation", 1, term)
            .asSequence()
            .sorted()
            .map {
                var doc = it.getArgument(3)
                doc.replace("\\n".toRegex(), "")

            }
            .joinToString(separator = "\n")
    }
}
