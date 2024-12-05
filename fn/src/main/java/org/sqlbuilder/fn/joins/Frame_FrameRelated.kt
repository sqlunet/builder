package org.sqlbuilder.fn.joins

import edu.berkeley.icsi.framenet.FrameIDNameType
import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.fn.types.FrameRelation
import org.sqlbuilder.fn.types.FrameRelation.add
import org.sqlbuilder.fn.types.FrameRelation.getSqlId
import java.util.*

data class Frame_FrameRelated(
    val frameid: Int,
    val frame2id: Int,
    val relation: String,
) : Insertable {

    // I N S E R T

    @RequiresIdFrom(type = FrameRelation::class)
    override fun dataRow(): String {
        return "$frameid,$frame2id,${getSqlId(relation)}"
    }

    override fun comment(): String {
        return "rel=$relation"
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as Frame_FrameRelated
        return frameid == that.frameid && frame2id == that.frame2id
    }

    override fun hashCode(): Int {
        return Objects.hash(frameid, frame2id)
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[relFR frameid=$frameid frame2id=$frame2id type=$relation]"
    }

    companion object {

        @JvmField
        val COMPARATOR: Comparator<Frame_FrameRelated> = Comparator
            .comparing<Frame_FrameRelated, String> { it.relation }
            .thenComparing<Int> { it.frameid }
            .thenComparing<Int> { it.frame2id }

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
