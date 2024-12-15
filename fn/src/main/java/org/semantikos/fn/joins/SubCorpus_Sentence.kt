package org.semantikos.fn.joins

import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.Insertable
import org.semantikos.fn.objects.Sentence
import org.semantikos.fn.objects.SubCorpus
import java.util.*

data class SubCorpus_Sentence(
    var subcorpus: SubCorpus,
    var sentenceid: Int,
) : Insertable {

    // I N S E R T

    @RequiresIdFrom(type = SubCorpus::class)
    override fun dataRow(): String {
        return "${subcorpus.getSqlId()},$sentenceid"
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as SubCorpus_Sentence
        return subcorpus == that.subcorpus && sentenceid == that.sentenceid
    }

    override fun hashCode(): Int {
        return Objects.hash(subcorpus, sentenceid)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[SUBCORPUS-SENT subcorpusid=$subcorpus sentenceid=$sentenceid]"
    }

    companion object {

        val COMPARATOR: Comparator<SubCorpus_Sentence> = Comparator
            .comparing<SubCorpus_Sentence, SubCorpus>({ it.subcorpus }, SubCorpus.COMPARATOR)
            .thenComparing<Int> { it.sentenceid }

        val SET = HashSet<SubCorpus_Sentence>()

        fun make(subcorpus: SubCorpus, sentence: Sentence): SubCorpus_Sentence {
            val ss = SubCorpus_Sentence(subcorpus, sentence.iD)
            SET.add(ss)
            return ss
        }
    }
}
