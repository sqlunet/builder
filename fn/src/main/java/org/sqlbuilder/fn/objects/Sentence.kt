package org.sqlbuilder.fn.objects

import edu.berkeley.icsi.framenet.SentenceType
import org.sqlbuilder.common.HasID
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.Utils.escape
import org.sqlbuilder.common.Utils.zeroableInt
import java.util.*
import kotlin.math.min

class Sentence private constructor(
    sentence: SentenceType,
) : HasID, Insertable {

    val iD: Int = sentence.getID()

    val corpusID: Int = sentence.getCorpID()

    val docID: Int = sentence.getDocID()

    private val paragno: Int = sentence.getParagNo()

    private val sentno: Int = sentence.getSentNo()

    private val apos: Int = sentence.getAPos()

    private val text: String = sentence.getText()

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val sentence = o as Sentence
        return iD == sentence.iD && corpusID == sentence.corpusID && docID == sentence.docID
    }

    override fun hashCode(): Int {
        return Objects.hash(iD, corpusID, docID)
    }

    // I N S E R T

    override fun dataRow(): String {
        return "$iD,${zeroableInt(corpusID)},${zeroableInt(docID)},$paragno,$sentno,'${escape(text)}',$apos"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[SENT id=$iD text=${ellipsis(text)}]"
    }

    companion object {

        private fun ellipsis(text: String): String {
            val max = 32
            val len = text.length
            return text.substring(0, min(max.toDouble(), len.toDouble()).toInt())
        }

        @JvmField
        val COMPARATOR: Comparator<Sentence> = Comparator
            .comparing<Sentence, Int> { it.iD }
            .thenComparing<Int> { it.docID }
            .thenComparing<Int> { it.corpusID }

        @JvmField
        val SET = HashSet<Sentence>()

        fun make(sentence: SentenceType): Sentence {
            val s = Sentence(sentence)
            SET.add(s)
            return s
        }
    }
}
