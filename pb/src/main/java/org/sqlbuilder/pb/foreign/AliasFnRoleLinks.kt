package org.sqlbuilder.pb.foreign

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.SetCollector

class AliasFnRoleLinks private constructor(
    links: Collection<String>,
) : AliasRoleLinks(links) {

    // N I D

    @RequiresIdFrom(type = AliasFnRoleLinks::class)
    override fun getIntId(): Int {
        return COLLECTOR[this]!!
    }

    companion object {

        @JvmField
        val COLLECTOR = SetCollector<AliasRoleLinks>(COMPARATOR)
        @JvmStatic
        fun make(links: Collection<String>): AliasFnRoleLinks {
            val t = AliasFnRoleLinks(normalize(links))
            COLLECTOR.add(t)
            return t
        }

        @Suppress("unused")
        @RequiresIdFrom(type = AliasFnRoleLinks::class)
        fun getIntId(links: AliasFnRoleLinks): Int {
            return COLLECTOR[links]!!
        }
    }
}