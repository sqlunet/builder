package org.sqlbuilder.fn.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import java.util.*

class Values {

    class Pos private constructor(
        private val pos: String
    ) : HasId, Insertable {

        @RequiresIdFrom(type = Pos::class)
        override fun getIntId(): Int? {
            return MAP[this]
        }

        override fun dataRow(): String {
            return "'$pos'"
        }

        companion object {

            val COMPARATOR: Comparator<Pos> = Comparator
                .comparing<Pos, String> { it.pos }

                val MAP = TreeMap<Pos, Int>(COMPARATOR)

                    fun make(pos: String, idx: Int): Pos {
                val p = Pos(pos)
                MAP.put(p, idx)
                return p
            }
        }
    }

    class CoreType private constructor(
        private val coretype: String
    ) : HasId, Insertable {

        @RequiresIdFrom(type = CoreType::class)
        override fun getIntId(): Int? {
            return MAP[this]
        }

        override fun dataRow(): String {
            return "'$coretype'"
        }

        companion object {

            val COMPARATOR: Comparator<CoreType> = Comparator
                .comparing<CoreType, String> { it.coretype }

                val MAP = TreeMap<CoreType, Int>(COMPARATOR)

                    fun make(coretype: String, idx: Int): CoreType {
                val t = CoreType(coretype)
                MAP.put(t, idx)
                return t
            }
        }
    }

    class LabelIType private constructor(private val labelitype: String) : HasId, Insertable {

        @RequiresIdFrom(type = LabelIType::class)
        override fun getIntId(): Int? {
            return MAP[this]
        }

        override fun dataRow(): String {
            return "'$labelitype'"
        }

        companion object {

            val COMPARATOR: Comparator<LabelIType> = Comparator
                .comparing<LabelIType, String> { it.labelitype }

                val MAP = TreeMap<LabelIType, Int>(COMPARATOR)

                    fun make(labelitype: String, idx: Int): LabelIType {
                val l = LabelIType(labelitype)
                MAP.put(l, idx)
                return l
            }
        }
    }
}
