package org.sqlbuilder.fn.types

import org.sqlbuilder.common.HasID
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.Utils.escape
import java.util.*

class Cxns private constructor(
    val id: Int,
    val name: String,
) : HasID, Insertable {

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val cxns = o as Cxns
        return id == cxns.id && name == cxns.name
    }

    override fun hashCode(): Int {
        return Objects.hash(id, name)
    }

    // I N S E R T

    override fun dataRow(): String {
        return "$id,'${escape(name)}'"
    }

    companion object {

        @JvmField
        val COMPARATOR: Comparator<Cxns> = Comparator
            .comparing<Cxns, String> { it.name }
            .thenComparing<Int> { it.id }

        @JvmField
        val SET = HashSet<Cxns>()

        fun make(id: Int, name: String): Cxns {
            val c = Cxns(id, name)
            SET.add(c)
            return c
        }
    }
}

/*
# cxnid, cxn
78, Ones_very_eyes
80, As.role
74, Subject-predicate
75, Head-complements
79, Bare_noun_phrase.role
76, Be_present-participle
73, Valence_sharing.raising
83, Superlative
117, Uniqueness
26, Supplement_specificational
105, Coordination
27, Supplement_ascriptional
112, Stripping
114, There_be_a_time_when
113, Bare_argument_ellipsis
102, Postpositive_adjective
31, Attributive_degree_modification
18, Comparison_equality
107, Noun-noun_compound
82, Determined_noun_phrase
111, TEMP_The_ubiquitous_noun
81, Modifier-head
*/

