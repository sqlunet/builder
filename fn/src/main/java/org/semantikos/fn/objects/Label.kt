package org.semantikos.fn.objects

import edu.berkeley.icsi.framenet.LabelType
import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.Insertable
import org.semantikos.common.Utils.nullableInt
import org.semantikos.common.Utils.zeroableInt
import org.semantikos.fn.types.LabelType.add
import org.semantikos.fn.types.LabelType.getIntId

class Label private constructor(label: LabelType, val layer: Layer) : Insertable, Comparable<Label> {

    private val name: String = label.name

    private val itypeid: Int? = if (label.itype == null) null else label.itype.intValue()

    private val feid: Int = label.feID

    private val start: Int = label.start

    private val end: Int = label.end

    @RequiresIdFrom(type = Layer::class)
    @RequiresIdFrom(type = org.semantikos.fn.types.LabelType::class)
    override fun dataRow(): String {
        return "${getIntId(name)},${nullableInt(itypeid)},${zeroableInt(feid)},${zeroableInt(start)},${zeroableInt(end)},${layer.getSqlId()}"
    }

    // O R D E R

    override fun compareTo(that: Label): Int {
        return COMPARATOR.compare(this, that)
    }

    // I N S E R T

    override fun comment(): String {
        return "type=$name"
    }

    override fun toString(): String {
        return "[LAB label=$name layer=$layer]"
    }

    companion object {

        val COMPARATOR: Comparator<Label> = Comparator
            .comparing<Label, String> { it.name }
            .thenComparing<Layer> { it.layer }
            .thenComparing<Int> { it.start }
            .thenComparing<Int> { it.end }

        val SET = HashSet<Label>()

        fun make(label: LabelType, layer: Layer): Label {
            val l = Label(label, layer)
            add(l.name)
            SET.add(l)
            return l
        }
    }
}
