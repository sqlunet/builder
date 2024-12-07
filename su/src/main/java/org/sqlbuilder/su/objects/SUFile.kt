package org.sqlbuilder.su.objects

import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.Resolvable
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.common.Utils.nullableDate
import org.sqlbuilder.common.Utils.nullableQuotedEscapedString
import java.io.File
import java.io.Serializable
import java.util.*

class SUFile private constructor(

    val filename: String,
    @Suppress("SameParameterValue") val fileVersion: String?,
    @Suppress("SameParameterValue") val fileDate: Date?,
) : HasId, Insertable, Serializable, Comparable<SUFile>, Resolvable<String, Int> {

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val sumoFile = o as SUFile
        return filename == sumoFile.filename
    }

    override fun hashCode(): Int {
        return Objects.hash(filename)
    }

    // O R D E R

    override fun compareTo(that: SUFile): Int {
        return COMPARATOR.compare(this, that)
    }

    // I N S E R T

    override fun dataRow(): String {
        return "${resolve()},${nullableQuotedEscapedString(filename)},${nullableQuotedEscapedString(fileVersion)},${nullableDate(fileDate)}"
    }

    // R E S O L V E

    fun resolve(): Int {
        return intId
    }

    //@RequiresIdFrom(type = SUMOFile.class)
    override fun getIntId(): Int {
        return COLLECTOR.invoke(this)
    }

    override fun resolving(): String {
        return filename
    }

    companion object {

        val COMPARATOR: Comparator<SUFile> = Comparator
            .comparing<SUFile, String> { it.filename }

        val COLLECTOR = SetCollector<SUFile>(COMPARATOR)

        fun make(filepath: String): SUFile {
            val file = File(filepath)
            val filename = file.getName()
            val f = SUFile(filename, null, null)
            COLLECTOR.add(f)
            return f
        }
    }
}
