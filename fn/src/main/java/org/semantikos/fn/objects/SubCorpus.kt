package org.semantikos.fn.objects

import edu.berkeley.icsi.framenet.SubCorpusType
import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.HasId
import org.semantikos.common.Insertable
import org.semantikos.common.SetCollector
import org.semantikos.common.Utils.escape

class SubCorpus private constructor(
    val name: String,
    val luid: Int
) : HasId, Insertable {

    @RequiresIdFrom(type = SubCorpus::class)
    override fun getIntId(): Int {
        return COLLECTOR.invoke(this)
    }

    // I N S E R T

    @RequiresIdFrom(type = SubCorpus::class)
    override fun dataRow(): String {
        return  "'${escape(name)}',$luid"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[SUBCORPUS name=$name]"
    }

    companion object {

        val COMPARATOR: Comparator<SubCorpus> = Comparator
            .comparing<SubCorpus, String> { it.name }
            .thenComparing<Int> { it.luid }

        val COLLECTOR = SetCollector<SubCorpus>(COMPARATOR)

        fun make(subcorpus: SubCorpusType, luid: Int): SubCorpus {
            val c = SubCorpus(subcorpus.getName(), luid)
            COLLECTOR.add(c)
            return c
        }
    }
}
