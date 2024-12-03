package org.sqlbuilder.bnc.objects

/*
1:WORD					2:POS   3:INFLECT       4:FREQ  5:RANGE 6:

%	                    NoC	    %	            6	    53	    0.64
@	                    @	    %	            6	    52	    0.64
@	                    @	    %s	            0	    1	    0.00

abandon                 NoC     :               1       53      0.87
abandon                 Verb    %               44      99      0.96
@                       @       abandon         12      98      0.94
@                       @       abandoned       26      97      0.96
@                       @       abandoning      5       90      0.93
@                       @       abandons        1       47      0.87
abandoned               Adj     :               4       88      0.92
abandoned-in-transit    Adj     :               0       1       0.00
abandoned/ignored       Adj     :               0       1       0.00
abandonedl              NoC     :               0       1       0.00
abandonemtn             NoC     :               0       1       0.00
abandoning              NoC     :               0       12      0.71
abandonment             NoC     %               5       89      0.92
@                       @       abandonment     5       89      0.92
@                       @       abandonments    0       1       0.00
*/

import org.sqlbuilder.common.IgnoreException
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.NotFoundException
import org.sqlbuilder.common.ParseException
import org.sqlbuilder.common.Utils.escape

open class BncRecord protected constructor(
    @JvmField val word: String,
    @JvmField protected val pos: Char,
    @JvmField protected val freq: Int,
    @JvmField protected val range: Int,
    @JvmField protected val dispersion: Float,
) : Insertable {

    // I N S E R T

    override fun dataRow(): String {
        return "'${escape(word)}','$pos',$freq,$range,$dispersion"
    }

    // T O S T R I N G

    override fun toString(): String {
        return " [$word,$pos] $freq $range $dispersion"
    }

    companion object {

        @JvmField
        val posMap = mapOf(
            ("Adj" to 'a'),
            ("Adv" to 'r'),
            ("Verb" to 'v'),
            ("NoC" to 'n'),
            // "VMod"
            // "NoP"
            // "NoP-"
            // "ClO",
            // "Conj",
            // "Det",
            // "DetP",
            // "Ex",
            // "Fore",
            // "Gen",
            // "Inf",
            // "Int",
            // "Lett",
            // "Neg",
            // "Num",
            // "Ord",
            // "Prep",
            // "Pron",
            // "Uncl",
        )

        @JvmStatic
        protected var lastLemma: String? = null

        @JvmStatic
        protected var lastPos: String? = null

        @JvmStatic
        @Throws(ParseException::class, NotFoundException::class, IgnoreException::class)
        fun parse(line: String): BncRecord {
            val fields = line.split("\\t+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (fields.size != 7) {
                throw ParseException("Number of fields is not 7")
            }

            // fields
            val field1: String = fields[1]
            val field2: String = fields[2]
            val inflectedForm = fields[3]

            // expand placeholders (do not move: this is a reference to previous line/chain of lines)
            val word: String = (if ("@" == field1) lastLemma!! else field1)
            val bncPos: String = (if ("@" == field2) lastPos!! else field2)

            lastLemma = word
            lastPos = bncPos

            // do not process variants
            if ("%" != inflectedForm || "%" == field1) {
                throw IgnoreException(inflectedForm)
            }

            // convert data
            val lemma: String = makeLemma(word)
            val pos: Char? = posMap[bncPos]
            if (pos == null) {
                throw NotFoundException(bncPos)
            }

            // freq data
            val freq = fields[4].toInt()
            val range = fields[5].toInt()
            val dispersion = fields[6].toFloat()

            return BncRecord(lemma, pos, freq, range, dispersion)
        }

        @JvmStatic
        fun makeLemma(word: String): String {
            var word = word
            word = word.trim { it <= ' ' }
            if (word.endsWith("*")) {
                word = word.substring(0, word.length - 1)
            }
            return word.replace(' ', '_')
        }
    }
}
