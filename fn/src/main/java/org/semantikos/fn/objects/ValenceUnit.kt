package org.semantikos.fn.objects

import edu.berkeley.icsi.framenet.ValenceUnitType
import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.HasId
import org.semantikos.common.Insertable
import org.semantikos.common.SetCollector
import org.semantikos.fn.types.FeType
import org.semantikos.fn.types.GfType
import org.semantikos.fn.types.PtType
import java.util.*

class ValenceUnit private constructor(
    vu: ValenceUnitType,
) : HasId, Comparable<ValenceUnit>, Insertable {

    val fE: String?

    val pT: String?

    val gF: String?

    init {
        val fe = vu.getFE()
        fE = if (fe == null || fe.isEmpty()) null else fe
        val pt = vu.getPT()
        pT = if (pt == null || pt.isEmpty()) null else pt
        val gf = vu.getGF()
        gF = if (gf == null || gf.isEmpty()) null else gf
    }

    @RequiresIdFrom(type = ValenceUnit::class)
    override fun getIntId(): Int {
        return COLLECTOR.invoke(this)
    }

    // I D E N T I T Y

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as ValenceUnit
        return fE == that.fE && pT == that.pT && gF == that.gF
    }

    override fun hashCode(): Int {
        return Objects.hash(fE, pT, gF)
    }

    // O R D E R

    override fun compareTo(other: ValenceUnit): Int {
        return COMPARATOR.compare(this, other)
    }

    // I N S E R T

    @RequiresIdFrom(type = ValenceUnit::class)
    @RequiresIdFrom(type = FeType::class)
    @RequiresIdFrom(type = PtType::class)
    @RequiresIdFrom(type = GfType::class)
    override fun dataRow(): String {
        return "${FeType.getSqlId(fE)},${PtType.getSqlId(pT)},${GfType.getSqlId(gF)}"
    }

    override fun comment(): String {
        return "fe=$fE,pt=$pT,gf=$gF"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "VU fe=$fE pt=$pT gf=$gF"
    }

    companion object {

        val COMPARATOR: Comparator<ValenceUnit> = compareBy<ValenceUnit, String?>(nullsFirst()) { it.fE }
            .thenBy(nullsFirst()) { it.pT }
            .thenBy(nullsFirst()) { it.gF }

        val COLLECTOR = SetCollector(COMPARATOR)

        fun make(vu: ValenceUnitType): ValenceUnit {
            val v = ValenceUnit(vu)
            if (v.fE != null) {
                FeType.COLLECTOR.add(v.fE)
            }
            if (v.pT != null) {
                PtType.COLLECTOR.add(v.pT)
            }
            if (v.gF != null) {
                GfType.COLLECTOR.add(v.gF)
            }
            COLLECTOR.add(v)
            return v
        }
    }
}
