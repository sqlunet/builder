package org.sqlbuilder.pb.foreign

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.SetCollector

class AliasVnRoleLinks private constructor(
    links: Collection<String>,
) : AliasRoleLinks(links) {

    // N I D

    @RequiresIdFrom(type = AliasVnRoleLinks::class)
    override fun getIntId(): Int {
        return COLLECTOR[this]!!
    }

    companion object {

        @JvmField
        val COLLECTOR = SetCollector<AliasRoleLinks>(COMPARATOR)
        @JvmStatic
        fun make(links: Collection<String>): AliasVnRoleLinks {
            val t = AliasVnRoleLinks(normalize(links))
            COLLECTOR.add(t)
            return t
        }

        @Suppress("unused")
        @RequiresIdFrom(type = AliasVnRoleLinks::class)
        fun getIntId(links: AliasVnRoleLinks): Int {
            return COLLECTOR[links]!!
        }
    }
}