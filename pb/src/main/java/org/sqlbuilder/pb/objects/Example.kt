package org.sqlbuilder.pb.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.common.Utils
import org.sqlbuilder.pb.PbNormalizer

class Example private constructor(
    private val roleSet: RoleSet,
    private val name: String,
    text: String,
) : HasId, Insertable, Comparable<Example> {

    private val text: String = PbNormalizer.normalize(text)

    val rels: MutableList<Rel> = ArrayList<Rel>()

    val args: MutableList<Arg> = ArrayList<Arg>()

    // N I D

    @RequiresIdFrom(type = Func::class)
    override fun getIntId(): Int {
        return COLLECTOR[this]!!
    }

    // O R D E R

    override fun compareTo(that: Example): Int {
        return COMPARATOR.compare(this, that)
    }

    // I N S E R T

    @RequiresIdFrom(type = RoleSet::class)
    override fun dataRow(): String {
        return "'${Utils.escape(name)}','${Utils.escape(text)}',${roleSet.intId}"
    }

    override fun comment(): String {
        return roleSet.name
    }

    override fun toString(): String {
        return "$roleSet[$name]"
    }

    companion object {

        private val COMPARATOR: Comparator<Example> = Comparator
            .comparing<Example, RoleSet> { it.roleSet }
            .thenComparing { it.name }
            .thenComparing { it.text }

        @JvmField
        val COLLECTOR = SetCollector<Example>(COMPARATOR)

        fun make(roleSet: RoleSet, name: String, text: String): Example {
            val e = Example(roleSet, name, text)

            COLLECTOR.add(e)
            return e
        }

        @Suppress("unused")
        @RequiresIdFrom(type = Func::class)
        fun getIntId(example: Example): Int {
            return COLLECTOR[example]!!
        }
    }
}
