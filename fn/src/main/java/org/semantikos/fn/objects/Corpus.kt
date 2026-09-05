package org.semantikos.fn.objects

import edu.berkeley.icsi.framenet.CorpDocType
import org.semantikos.common.HasID
import org.semantikos.common.Insertable
import org.semantikos.common.Utils.escape
import org.semantikos.common.Utils.nullableInt
import java.util.*

class Corpus private constructor(
    corpus: CorpDocType,
    private val luid: Int?,
) : HasID, Insertable {

    val iD: Int = corpus.getID()

    val name: String = corpus.getName()

    private val description: String = corpus.getDescription()

    // I D E N T I T Y

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as Corpus
        return iD == that.iD
    }

    override fun hashCode(): Int {
        return Objects.hash(iD)
    }

    // I N S E R T

    override fun dataRow(): String {
        return "$iD,'$name','${escape(description)}',${nullableInt(luid)}"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[CORPUS id=$iD name=$name]"
    }

    companion object {

        val COMPARATOR: Comparator<Corpus> = Comparator
            .comparing<Corpus, String> { it.name }
            .thenComparing<Int> { it.iD }

        val SET = HashSet<Corpus>()

        fun make(corpus: CorpDocType, luid: Int?): Corpus {
            val c = Corpus(corpus, luid)
            SET.add(c)
            return c
        }
    }
}
