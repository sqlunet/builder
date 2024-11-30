package org.sqlbuilder.fn.joins

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.fn.objects.Sentence
import org.sqlbuilder.fn.objects.SubCorpus

class SubCorpus_Sentence private constructor(
    subcorpus: SubCorpus,
    sentenceid: Int,
) : Pair<SubCorpus, Int>(subcorpus, sentenceid), Insertable {

    // I N S E R T

    @RequiresIdFrom(type = SubCorpus::class)
    override fun dataRow(): String {
        return "${first.getSqlId()},$second"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[SUBCORPUS-SENT subcorpusid=$first sentenceid=$second]"
    }

    companion object {

        @JvmField
        val COMPARATOR: Comparator<SubCorpus_Sentence> = Comparator
            .comparing<SubCorpus_Sentence, SubCorpus>({ it.first }, SubCorpus.COMPARATOR)
            .thenComparing<Int> { it.second }

        @JvmField
        val SET = HashSet<SubCorpus_Sentence>()

        @JvmStatic
        fun make(subcorpus: SubCorpus, sentence: Sentence): SubCorpus_Sentence {
            val ss = SubCorpus_Sentence(subcorpus, sentence.iD)
            SET.add(ss)
            return ss
        }
    }
}
