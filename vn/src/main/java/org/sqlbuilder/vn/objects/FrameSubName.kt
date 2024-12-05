package org.sqlbuilder.vn.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.SetCollector
import java.util.*

class FrameSubName private constructor(
    subname: String,
) : HasId, Insertable, Comparable<FrameSubName> {

    val subName: String = subname.trim { it <= ' ' }.uppercase(Locale.getDefault()).replace("\\s+".toRegex(), " ")

    override fun getIntId(): Int {
        return COLLECTOR.invoke(this)
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (o !is FrameSubName) {
            return false
        }
        val that = o
        return subName == that.subName
    }

    override fun hashCode(): Int {
        return subName.hashCode()
    }

    // O R D E R I N G

    override fun compareTo(that: FrameSubName): Int {
        return COMPARATOR.compare(this, that)
    }

    // I N S E R T

    override fun dataRow(): String {
        return "'$subName'"
    }

    companion object {

        val COMPARATOR: Comparator<FrameSubName> = Comparator.comparing<FrameSubName, String> { it.subName }

        @JvmField
        val COLLECTOR: SetCollector<FrameSubName> = SetCollector<FrameSubName>(COMPARATOR)

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
