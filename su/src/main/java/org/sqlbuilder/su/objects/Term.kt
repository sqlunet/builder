package org.sqlbuilder.su.objects

import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.Resolvable
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.common.Utils.quotedEscapedString
import java.io.Serializable
import java.util.*

class Term private constructor(
    val term: String,
) : HasId, Insertable, Serializable, Comparable<Term>, Resolvable<String, Int> {

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val sumoTerm = o as Term
        return term == sumoTerm.term
    }

    override fun hashCode(): Int {
        return Objects.hash(term)
    }

    // O R D E R

    override fun compareTo(that: Term): Int {
        return COMPARATOR.compare(this, that)
    }

    // T O S T R I N G

    override fun toString(): String {
        return term
    }

    // I N S E R T

    override fun dataRow(): String {
        return "${resolve()},${quotedEscapedString(term)}"
    }

    override fun comment(): String {
        return term
    }

    // R E S O L V E

    fun resolve(): Int {
        return intId
    }

    override fun getIntId(): Int {
        return COLLECTOR.invoke(this)
    }

    override fun resolving(): String {
        return term
    }

    companion object {

        val COMPARATOR: Comparator<Term> = Comparator
            .comparing<Term, String> { it.term }

        val COLLECTOR = SetCollector<Term>(COMPARATOR)

        @Suppress("unused")
        val wellKnownTerms = arrayOf<String>(
            "subclass", "subrelation", "instance", "disjoint",
            "domain", "partition",
            "attribute", "property",
            "subAttribute", "subProcess",
            "equal", "inverse",
            "=>", "<=>",
            "contains", "element", "subset", "component", "part", "piece",
            "format", "documentation",
            "Relation", "Predicate", "Function", "Class"
        )

        @Throws(IllegalArgumentException::class, StringIndexOutOfBoundsException::class)
        fun parse(line: String): String {
            // split into fields
            // Each SUMO concept is designated with the prefix '&%'. Note
            // that each concept also has a suffix, '=', ':', '+', '[', ']' or '@', which indicates
            // the precise relationship between the SUMOTerm concept and the WordNet synset.
            // The symbols '=', '+', and '@' mean, respectively, that the WordNet synset
            // is equivalent in meaning to the SUMOTerm concept, is subsumed by the SUMOTerm
            // concept or is an instance of the SUMOTerm concept. ':', '[', and ']' are the
            // complements of those relations. For example, a mapping expressed as
            // ; (%ComplementFn &%Motion)+ now appears as &%Motion[
            // Note also that ']' has not currently been needed.

            val breakPos = line.lastIndexOf("&%")
            require(breakPos != -1) { line }
            try {
                val position = breakPos + 2
                return line.substring(position, line.length - 1)
            } catch (_: Exception) {
                System.err.println(line)
                throw IllegalArgumentException(line)
            }
        }

        fun make(term: String): Term {
            require(!term.isEmpty()) { "Empty term" }

            val t = Term(term.trim { it <= ' ' })
            COLLECTOR.add(t)
            return t
        }
    }
}
