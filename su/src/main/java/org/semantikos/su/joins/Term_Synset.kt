package org.semantikos.su.joins

import org.semantikos.common.AlreadyFoundException
import org.semantikos.common.Insertable
import org.semantikos.common.Utils.nullableInt
import org.semantikos.su.objects.Term
import org.semantikos.su.objects.Term.Companion.make
import java.io.Serializable
import java.util.*

class Term_Synset private constructor(
    val term: Term,
    val synsetId: Long,
    val posId: Char,
    val mapType: String,
) : Insertable, Serializable, Comparable<Term_Synset> {

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as Term_Synset
        return synsetId == that.synsetId && posId == that.posId && term == that.term
    }

    override fun hashCode(): Int {
        return Objects.hash(synsetId, posId, term)
    }

    // O R D E R

    override fun compareTo(that: Term_Synset): Int {
        return COMPARATOR.compare(this, that)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "$term -> $synsetId [$posId] ($mapType)"
    }

    // I N S E R T

    override fun dataRow(): String {
        return "$synsetId,'$posId','$mapType',${nullableInt(resolvedTermId())}"
    }

    override fun comment(): String {
        return term.term
    }

    // R E S O L V E

    fun resolvedTermId(): Int {
        return term.resolve()
    }

    companion object {

        private val COMPARATOR: Comparator<Term_Synset> = Comparator
            .comparing<Term_Synset, Term> { it.term }
            .thenComparing<Long> { it.synsetId }
            .thenComparing<Char> { it.posId }
            .thenComparing<String> { it.mapType }

        val SET = TreeSet<Term_Synset>()

        @Throws(IllegalArgumentException::class)
        fun parse(termstr: String, line: String): Term_Synset {
            // split into fields
            // Each SUMOTerm concept is designated with the prefix '&%'. Note
            // that each concept also has a suffix, '=', ':', '+', '[', ']' or '@', which indicates
            // the precise relationship between the SUMOTerm concept and the WordNet synset.
            // The symbols '=', '+', and '@' mean, respectively, that the WordNet synset
            // is equivalent in meaning to the SUMOTerm concept, is subsumed by the SUMOTerm
            // concept or is an instance of the SUMOTerm concept. ':', '[', and ']' are the
            // complements of those relations. For example, a mapping expressed as
            // ; (%ComplementFn &%Motion)+ now appears as &%Motion[
            // Note also that ']' has not currently been needed.
            // 00003939 00 a
            val pos = line[12]
            val breakPos = line.indexOf(' ')
            val offsetField = line.substring(0, breakPos)
            val synsetId = offsetField.toLong()
            val term = make(termstr)
            val mapType = line.substring(line.length - 1)
            return make(term, synsetId, pos, mapType)
        }

        @Throws(AlreadyFoundException::class)
        fun make(term: Term, synsetId: Long, pos: Char, mapType: String): Term_Synset {
            val map = Term_Synset(term, synsetId, pos, mapType)
            val wasThere: Boolean = !SET.add(map)
            if (wasThere) {
                throw AlreadyFoundException(map.toString())
            }
            return map
        }
    }
}
