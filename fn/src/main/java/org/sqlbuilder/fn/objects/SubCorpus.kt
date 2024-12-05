package org.sqlbuilder.fn.objects

import edu.berkeley.icsi.framenet.SubCorpusType
import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.common.Utils.escape

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

        @JvmField
        val COMPARATOR: Comparator<SubCorpus> = Comparator
            .comparing<SubCorpus, String> { it.name }
            .thenComparing<Int> { it.luid }

        @JvmField
        val COLLECTOR = SetCollector<SubCorpus>(COMPARATOR)

        @JvmStatic
        fun make(subcorpus: SubCorpusType, luid: Int): SubCorpus {
            val c = SubCorpus(subcorpus.getName(), luid)
            COLLECTOR.add(c)
            return c
        }
    }
}
