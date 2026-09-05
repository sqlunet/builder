package org.semantikos.su.objects

import org.semantikos.common.HasId
import org.semantikos.common.Insertable
import org.semantikos.common.Resolvable
import org.semantikos.common.SetCollector
import org.semantikos.common.Utils.nullableDate
import org.semantikos.common.Utils.nullableQuotedEscapedString
import java.io.File
import java.io.Serializable
import java.util.*

class SUFile private constructor(

    val filename: String,
    @Suppress("SameParameterValue") val fileVersion: String?,
    @Suppress("SameParameterValue") val fileDate: Date?,
) : HasId, Insertable, Serializable, Comparable<SUFile>, Resolvable<String, Int> {

    // I D E N T I T Y

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val sumoFile = other as SUFile
        return filename == sumoFile.filename
    }

    override fun hashCode(): Int {
        return Objects.hash(filename)
    }

    // O R D E R

    override fun compareTo(other: SUFile): Int {
        return COMPARATOR.compare(this, other)
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
