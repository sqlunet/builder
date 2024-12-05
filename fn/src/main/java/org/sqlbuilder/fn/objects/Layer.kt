package org.sqlbuilder.fn.objects

import edu.berkeley.icsi.framenet.LayerType
import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.fn.types.LayerType.add
import org.sqlbuilder.fn.types.LayerType.getIntId

class Layer private constructor(
    layer: LayerType,
    annosetid: Int,
) : HasId, Insertable, Comparable<Layer> {

    val name: String = layer.getName()

    private val rank: Int = layer.getRank()

    val annosetid: Long = annosetid.toLong()

    @RequiresIdFrom(type = Layer::class)
    override fun getIntId(): Int {
        return COLLECTOR.invoke(this)
    }

    // O R D E R

    override fun compareTo(that: Layer): Int {
        return COMPARATOR.compare(this, that)
    }

    // I N S E R T

    @RequiresIdFrom(type = Layer::class)
    override fun dataRow(): String {
        return "$intId,${getIntId(name)},$rank,$annosetid"
    }

    override fun comment(): String {
        return "type=$name"
    }

    override fun toString(): String {
        return "[LAY name=$name annosetid=$annosetid]"
    }

    companion object {

        val COMPARATOR: Comparator<Layer> = Comparator
            .comparing<Layer, String> { it.name }
            .thenComparing<Long> { it.annosetid }

        @JvmField
        val COLLECTOR = SetCollector<Layer>(COMPARATOR)

        fun make(layer: LayerType, annosetid: Int): Layer {
            val l = Layer(layer, annosetid)
            add(l.name)
            COLLECTOR.add(l)
            return l
        }
    }
}
