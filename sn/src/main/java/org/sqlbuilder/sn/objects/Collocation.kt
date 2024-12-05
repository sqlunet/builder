package org.sqlbuilder.sn.objects

import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.ParseException
import org.sqlbuilder.common.Resolvable
import org.sqlbuilder.common.Utils.nullableQuotedEscapedString
import org.sqlbuilder.sn.SnLemmaPosOffsetResolvable
import org.sqlbuilder.sn.SnSensekeyResolvable
import org.sqlbuilder.sn.SnSensekeyResolved
import kotlin.Throws

typealias SnCollocationResolvable = Pair<SnSensekeyResolvable, SnSensekeyResolvable>
typealias SnCollocationResolved = Pair<SnSensekeyResolved, SnSensekeyResolved>

class Collocation private constructor(
    val offset1: Int,
    val pos1: Char,
    val word1: String,
    val offset2: Int,
    val pos2: Char, val word2: String,
) : Insertable, Resolvable<SnCollocationResolvable, SnCollocationResolved> {

    @JvmField
    var sensekey1: String? = null

    @JvmField
    var sensekey2: String? = null

    // I N S E R T

    override fun dataRow(): String {
        return "${nullableQuotedEscapedString(sensekey1)},${nullableQuotedEscapedString(sensekey2)}"
    }

    override fun comment(): String {
        return "$word1,$pos1,$offset1,$word2,$pos2,$offset2"
    }

    // R E S O L V E

    fun resolveOffsets(skResolver: (SnLemmaPosOffsetResolvable) -> String?): Boolean {
        val sk1 = skResolver.invoke(SnLemmaPosOffsetResolvable(word1, pos1, offset1))
        val resolved1 = sk1 != null
        if (!resolved1) {
            println("[RK] $word1 $pos1 $offset1")
        }
        if (resolved1) {
            sensekey1 = sk1
        }
        val sk2 = skResolver.invoke(SnLemmaPosOffsetResolvable(word2, pos2, offset2))
        val resolved2 = sk2 != null
        if (!resolved2) {
            println("[RK] $word2, $pos2, $offset2")
        }
        if (resolved2) {
            sensekey2 = sk2
        }
        return resolved1 && resolved2
    }

    override fun resolving(): SnCollocationResolvable {
        return SnCollocationResolvable(sensekey1.toString(), sensekey2.toString())
    }

    companion object {

        val COMPARATOR: Comparator<Collocation> = Comparator
            .comparing<Collocation, String>({ it.sensekey1 }, nullsFirst(naturalOrder()))
            .thenComparing<String>({ it.sensekey2 }, nullsFirst(naturalOrder()))

        @JvmStatic
        @Throws(ParseException::class)
        fun parse(line: String): Collocation {
            try {
                val fields = line.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val synset1Id = fields[0].substring(0, fields[0].length - 1).toInt()
                val synset2Id = fields[1].substring(0, fields[1].length - 1).toInt()
                val lemma1 = fields[2]
                val pos1 = fields[3][0]
                val lemma2 = fields[4]
                val pos2 = fields[5][0]
                return make(synset1Id, pos1, lemma1, synset2Id, pos2, lemma2)
            } catch (e: Exception) {
                throw ParseException(e.message!!)
            }
        }

        private fun makeLemma(word: String): String {
            return word.trim { it <= ' ' }.replace('_', ' ')
        }

        fun make(offset1: Int, pos1: Char, lemma1: String, offset2: Int, pos2: Char, lemma2: String): Collocation {
            return Collocation(offset1, pos1, makeLemma(lemma1), offset2, pos2, makeLemma(lemma2))
        }
    }
}
