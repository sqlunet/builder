package org.sqlbuilder.vn.joins

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.vn.objects.Frame
import org.sqlbuilder.vn.objects.FrameExample
import java.util.*

class Frame_Example private constructor(
    val frame: Frame,
    val example: FrameExample,
) : Insertable, Comparable<Frame_Example> {

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as Frame_Example
        return frame == that.frame && example == that.example
    }

    override fun hashCode(): Int {
        return Objects.hash(frame, example)
    }

    // O R D E R I N G

    override fun compareTo(that: Frame_Example): Int {
        return COMPARATOR.compare(this, that)
    }

    // I N S E R T

    @RequiresIdFrom(type = Frame::class)
    @RequiresIdFrom(type = FrameExample::class)
    override fun dataRow(): String {
        return "${frame.intId},${example.intId}"
    }

    companion object {

        val COMPARATOR: Comparator<Frame_Example> = Comparator
            .comparing<Frame_Example, Frame> { it.frame }
            .thenComparing<FrameExample> { it.example }

        val SET = HashSet<Frame_Example>()

        fun make(frame: Frame, example: FrameExample): Frame_Example {
            val m = Frame_Example(frame, example)
            SET.add(m)
            return m
        }
    }
}
