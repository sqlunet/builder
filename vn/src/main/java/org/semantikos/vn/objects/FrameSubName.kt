package org.semantikos.vn.objects

import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.HasId
import org.semantikos.common.Insertable
import org.semantikos.common.SetCollector
import java.util.*

class FrameSubName private constructor(
    subname: String,
) : HasId, Insertable, Comparable<FrameSubName> {

    val subName: String = subname.trim { it <= ' ' }.uppercase(Locale.getDefault()).replace("\\s+".toRegex(), " ")

    override fun getIntId(): Int {
        return COLLECTOR.invoke(this)
    }

    // I D E N T I T Y

    override fun equals(other: Any?): Boolean {
        return other is FrameSubName && subName == other.subName
    }

    override fun hashCode(): Int {
        return subName.hashCode()
    }

    // O R D E R I N G

    override fun compareTo(other: FrameSubName): Int {
        return COMPARATOR.compare(this, other)
    }

    // I N S E R T

    override fun dataRow(): String {
        return "'$subName'"
    }

    companion object {

        val COMPARATOR: Comparator<FrameSubName> = Comparator.comparing { it.subName }

        val COLLECTOR: SetCollector<FrameSubName> = SetCollector(COMPARATOR)

        // C O N S T R U C T O R
        fun make(subname: String): FrameSubName {
            val s = FrameSubName(subname)
            COLLECTOR.add(s)
            return s
        }

        @RequiresIdFrom(type = FrameSubName::class)
        fun getIntId(subname: FrameSubName?): Int? {
            return if (subname == null) null else COLLECTOR.invoke(subname)
        }
    }
}
