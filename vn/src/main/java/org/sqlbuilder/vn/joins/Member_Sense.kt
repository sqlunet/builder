package org.sqlbuilder.vn.joins

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.Resolvable
import org.sqlbuilder.common.Utils.nullableFloat
import org.sqlbuilder.common.Utils.nullableInt
import org.sqlbuilder.common.Utils.nullableQuotedString
import org.sqlbuilder.vn.objects.Sensekey
import org.sqlbuilder.vn.objects.VnClass
import org.sqlbuilder.vn.objects.Word
import java.util.*
import java.util.AbstractMap.SimpleEntry

class Member_Sense private constructor(
    val member: Class_Word,
    private val sensenum: Int,
    val sensekey: Sensekey?,
    private val quality: Float?,
) : Insertable, Resolvable<String, SimpleEntry<Int, Int>?>, Comparable<Member_Sense> {

    val memberClass: VnClass
        get() = member.clazz

    val memberWord: Word
        get() = member.word

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as Member_Sense
        return member == that.member && sensekey == that.sensekey
    }

    override fun hashCode(): Int {
        return Objects.hash(member, sensekey)
    }

    // O R D E R I N G

    override fun compareTo(that: Member_Sense): Int {
        return COMPARATOR.compare(this, that)
    }

    // I N S E R T

    @RequiresIdFrom(type = VnClass::class)
    @RequiresIdFrom(type = Word::class)
    override fun dataRow(): String {
        return "${member.clazz.intId},${member.word.intId},$sensenum,${nullableQuotedString(sensekey)},${nullableFloat(quality)}"
    }

    override fun comment(): String {
        return "${member.clazz.name},${member.word.word}"
    }

    // R E S O L V E

    override fun resolving(): String? {
        return if (sensekey == null) null else sensekey.sensekey
    }

    // T O S T R I N G

    override fun toString(): String {
        return "${super.toString()}-$sensenum-$sensekey-$quality"
    }

    companion object {

        @JvmField
        val COMPARATOR: Comparator<Member_Sense> = Comparator
            .comparing<Member_Sense, VnClass> { it.memberClass }
            .thenComparing<Word> { it.memberWord }
            .thenComparing<Sensekey?>({ it.sensekey }, nullsFirst(naturalOrder<Sensekey>()))

        @JvmField
        val SET = HashSet<Member_Sense>()

        @JvmField
        val RESOLVE_RESULT_STRINGIFIER = { r: SimpleEntry<Int, Int>? ->
            if (r == null) "NULL,NULL" else "${nullableInt(r.key)},${nullableInt(r.value)}"
        }

        @JvmStatic
        fun make(member: Class_Word, sensenum: Int, sensekey: Sensekey?, quality: Float?): Member_Sense {
            val m = Member_Sense(member, sensenum, sensekey, quality)
            SET.add(m)
            return m
        }
    }
}
