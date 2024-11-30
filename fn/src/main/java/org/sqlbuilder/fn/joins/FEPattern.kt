package org.sqlbuilder.fn.joins

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.fn.objects.FERealization
import org.sqlbuilder.fn.objects.ValenceUnit

class FEPattern private constructor(
    fer: FERealization, vu: ValenceUnit,
) : Pair<FERealization, ValenceUnit>(fer, vu), Insertable {

    val fer: FERealization
        get() = first

    val feName: String
        get() = first.fEName

    val luId: Int
        get() = first.luId

    // I N S E R T

    @RequiresIdFrom(type = FERealization::class)
    @RequiresIdFrom(type = ValenceUnit::class)
    override fun dataRow(): String {
        return "${first.getSqlId()},${second.getSqlId()}"
    }

    override fun comment(): String {
        return "luid=${first.luId},fe=${first.fEName},vu={${second.fE},${second.pT},${second.gF}}"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[FER-VU fer=$first vu=$second]"
    }

    companion object {

        @JvmField
        val COMPARATOR: Comparator<FEPattern> = Comparator
            .comparing<FEPattern, Int> { it.luId }
            .thenComparing<String> { it.feName }
            .thenComparing<ValenceUnit> { it.second }

        @JvmField
        val SET = HashSet<FEPattern>()

        @JvmStatic
        fun make(fer: FERealization, vu: ValenceUnit): FEPattern {
            val vr = FEPattern(fer, vu)
            SET.add(vr)
            return vr
        }
    }
}
