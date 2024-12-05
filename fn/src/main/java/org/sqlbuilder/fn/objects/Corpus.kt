package org.sqlbuilder.fn.objects

import edu.berkeley.icsi.framenet.CorpDocType
import org.sqlbuilder.common.HasID
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.Utils.escape
import org.sqlbuilder.common.Utils.nullableInt
import java.util.*

class Corpus private constructor(
    corpus: CorpDocType,
    private val luid: Int?,
) : HasID, Insertable {

    val iD: Int = corpus.getID()

    val name: String = corpus.getName()

    private val description: String = corpus.getDescription()

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as Corpus
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
