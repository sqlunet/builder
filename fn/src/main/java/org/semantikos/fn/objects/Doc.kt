package org.semantikos.fn.objects

import edu.berkeley.icsi.framenet.CorpDocType
import org.semantikos.common.HasID
import org.semantikos.common.Insertable
import org.semantikos.common.Utils.escape
import java.util.*

class Doc private constructor(
    doc: CorpDocType.Document,
    corpus: CorpDocType,
) : HasID, Insertable {

    val iD: Int = doc.getID()

    val name: String = doc.getName()

    private val description: String = doc.getDescription()

    private val corpusid: Int = corpus.getID()

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as Doc
        return iD == that.iD
    }

    override fun hashCode(): Int {
        return Objects.hash(iD)
    }

    // I N S E R T

    override fun dataRow(): String {
        return "$iD,'$name','${escape(description)}',$corpusid"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[DOC id=$iD corpusid=$corpusid]"
    }

    companion object {

        val COMPARATOR: Comparator<Doc> = Comparator
            .comparing<Doc, String> { it.name }
            .thenComparing<Int> { it.iD }

        val SET = HashSet<Doc>()

        fun make(doc: CorpDocType.Document, corpus: CorpDocType) {
            val d = Doc(doc, corpus)
            SET.add(d)
        }
    }
}
