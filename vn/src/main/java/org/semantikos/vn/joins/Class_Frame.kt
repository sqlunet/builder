package org.semantikos.vn.joins

import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.Insertable
import org.semantikos.vn.objects.Frame
import org.semantikos.vn.objects.VnClass
import java.util.*

class Class_Frame private constructor(
    val clazz: VnClass,
    val frame: Frame,
) : Insertable, Comparable<Class_Frame> {

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as Class_Frame
        return clazz == that.clazz && frame == that.frame
    }

    override fun hashCode(): Int {
        return Objects.hash(clazz, frame)
    }

    // O R D E R

    override fun compareTo(that: Class_Frame): Int {
        return COMPARATOR.compare(this, that)
    }

    // I N S E R T

    @RequiresIdFrom(type = VnClass::class)
    @RequiresIdFrom(type = Frame::class)
    override fun dataRow(): String {
        return "${clazz.intId},${frame.intId}"
    }

    override fun comment(): String {
        return "${clazz.name},${frame.name}"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "class=$clazz frame=$frame"
    }

    companion object {

        val COMPARATOR: Comparator<Class_Frame> = Comparator
            .comparing<Class_Frame, VnClass> { it.clazz }
            .thenComparing<Frame> { it.frame }

        val SET = HashSet<Class_Frame>()

        fun make(clazz: VnClass, frame: Frame): Class_Frame {
            val m = Class_Frame(clazz, frame)
            SET.add(m)
            return m
        }
    }
}
