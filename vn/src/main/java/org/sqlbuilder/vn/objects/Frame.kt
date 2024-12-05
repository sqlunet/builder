package org.sqlbuilder.vn.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.common.Utils.nullableInt
import org.sqlbuilder.common.Utils.nullableQuotedString
import org.sqlbuilder.vn.objects.FrameSubName.Companion.getIntId
import org.xml.sax.SAXException
import java.io.IOException
import java.util.*
import javax.xml.parsers.ParserConfigurationException

class Frame private constructor(
    val descriptionNumber: String,
    val descriptionXTag: String,
    descriptionPrimary: String,
    descriptionSecondary: String?,
    syntax: String, semantics: String,
) : HasId, Insertable, Comparable<Frame> {

    val name: FrameName = FrameName.make(descriptionPrimary)

    val subName: FrameSubName? = if (descriptionSecondary == null || descriptionSecondary.isEmpty()) null else FrameSubName.make(descriptionSecondary)

    val syntax: Syntax = Syntax.make(syntax)

    val semantics: Semantics = Semantics.make(semantics)

    override fun getIntId(): Int {
        return COLLECTOR.invoke(this)
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as Frame
        return descriptionNumber == that.descriptionNumber && descriptionXTag == that.descriptionXTag && name == that.name && subName == that.subName && syntax == that.syntax && semantics == that.semantics
    }

    override fun hashCode(): Int {
        return Objects.hash(descriptionNumber, descriptionXTag, name, subName, syntax, semantics)
    }

    // O R D E R I N G

    override fun compareTo(that: Frame): Int {
        return COMPARATOR.compare(this, that)
    }

    // T O S T R I N G

    override fun toString(): String {
        return " number=$descriptionNumber tag=$descriptionXTag description1=$name description2=$subName syntax=$syntax semantics=$semantics"
    }

    // I N S E R T

    @RequiresIdFrom(type = FrameName::class)
    @RequiresIdFrom(type = FrameSubName::class)
    @RequiresIdFrom(type = Syntax::class)
    @RequiresIdFrom(type = Semantics::class)
    override fun dataRow(): String {
        return "${nullableQuotedString<String?>(descriptionNumber)},'$descriptionXTag',${name.intId},${nullableInt(getIntId(subName))},${syntax.intId},${semantics.intId}"
    }

    companion object {

        val COMPARATOR: Comparator<Frame> = Comparator
            .comparing<Frame, FrameName> { it.name }
            .thenComparing<FrameSubName>({ it.subName }, Comparator.nullsFirst<FrameSubName>(Comparator.naturalOrder()))
            .thenComparing<String> { it.descriptionNumber }
            .thenComparing<String> { it.descriptionXTag }
            .thenComparing<Syntax> { it.syntax }
            .thenComparing<Semantics> { it.semantics }

        val COLLECTOR: SetCollector<Frame> = SetCollector<Frame>(COMPARATOR)

        @Throws(ParserConfigurationException::class, SAXException::class, IOException::class)
        fun make(descriptionNumber: String, descriptionXTag: String, descriptionPrimary: String, descriptionSecondary: String?, syntax: String, semantics: String): Frame {
            val f = Frame(descriptionNumber, descriptionXTag, descriptionPrimary, descriptionSecondary, syntax, semantics)
            COLLECTOR.add(f)
            return f
        }
    }
}
