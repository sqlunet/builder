package org.sqlbuilder.bnc.objects

import org.sqlbuilder.common.IgnoreException
import org.sqlbuilder.common.NotFoundException
import org.sqlbuilder.common.ParseException
import org.sqlbuilder.common.Utils.escape

class BncExtendedRecord(
    lemma: String,
    pos: Char,
    freq: Int,
    range: Int,
    dispersion: Float,
    val freq2: Int,
    val range2: Int,
    val dispersion2: Float,
    val lL: Float
) : BncRecord(lemma, pos, freq, range, dispersion) {

    // I N S E R T

    override fun dataRow(): String {
        return "'${escape(word)}','$pos',$freq,$range,$dispersion,$freq2,$range2,$dispersion2,$lL"
    }

    // T O S T R I N G

    override fun toString(): String {
        return super.toString() + " " + freq2 + " " + range2 + " " + dispersion2 + " " + lL
    }

    companion object {

        @JvmStatic
        @Throws(ParseException::class, NotFoundException::class, IgnoreException::class)
        fun parse(line: String): BncExtendedRecord {
            val fields = line.split("\\t+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (fields.size != 12) {
                throw ParseException("Number of fields is not 12")
            }

            // fields
            val field1: String = fields[1]
            val field2: String = fields[2]
            val inflectedForm = fields[3]

            // expand placeholders (do not move: this is a reference to previous line/chain of lines)
            val word = if ("@" == field1) lastLemma else field1
            val bncPos = if ("@" == field2) lastPos else field2

            lastLemma = word
            lastPos = bncPos

            // do not process variants
            if ("%" != inflectedForm || "%" == field1) {
                throw IgnoreException(inflectedForm)
            }

            // convert data
            val lemma = makeLemma(word!!)
            val pos: Char = posMap[bncPos]!!

            // data
            val freq = fields[4].toInt()
            val range = fields[5].toInt()
            val dispersion = fields[6].toFloat()

            // xdata
            val comp = fields[7]
            var lL = fields[8].toFloat() * -1
            if (comp == "-") {
                lL *= -1f
            }
            val freq2 = fields[9].toInt()
            val range2 = fields[10].toInt()
            val dispersion2 = fields[11].toFloat()

            return BncExtendedRecord(lemma, pos, freq, range, dispersion, freq2, range2, dispersion2, lL)
        }
    }
}
