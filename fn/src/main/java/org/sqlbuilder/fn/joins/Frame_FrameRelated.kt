package org.sqlbuilder.fn.joins

import edu.berkeley.icsi.framenet.FrameIDNameType
import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.fn.types.FrameRelation
import org.sqlbuilder.fn.types.FrameRelation.add
import org.sqlbuilder.fn.types.FrameRelation.getSqlId

class Frame_FrameRelated private constructor(
    frameid: Int,
    frame2id: Int,
    val relation: String,
) : Pair<Int, Int>(frameid, frame2id), Insertable {

    // I N S E R T

    @RequiresIdFrom(type = FrameRelation::class)
    override fun dataRow(): String {
        return "$first,$second,${getSqlId(relation)}"
    }

    override fun comment(): String {
        return "rel=$relation"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[relFR frameid=$first frame2id=$second type=$relation]"
    }

    companion object {

        @JvmField
        val COMPARATOR: Comparator<Frame_FrameRelated> = Comparator
            .comparing<Frame_FrameRelated, String> { it.relation }
            .thenComparing<Int> { it.first }
            .thenComparing<Int> { it.second }

        @JvmField
        val SET = HashSet<Frame_FrameRelated>()

        @JvmStatic
        fun make(frameid: Int, frame2: FrameIDNameType, relation: String): Frame_FrameRelated {
            val ff = Frame_FrameRelated(frameid, frame2.getID(), relation)
            add(relation)
            SET.add(ff)
            return ff
        }
    }
}
