package org.sqlbuilder.pb.foreign

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.SetCollector

class AliasFnFeLinks private constructor(
    links: Collection<String>,
) : AliasRoleLinks(links) {

    // N I D

    @RequiresIdFrom(type = AliasFnFeLinks::class)
    override fun getIntId(): Int {
        return COLLECTOR.apply(this)
    }

    companion object {

        @JvmField
        val COLLECTOR = SetCollector<AliasRoleLinks>(COMPARATOR)
        @JvmStatic
        fun make(links: Collection<String>): AliasFnFeLinks {
            val t = AliasFnFeLinks(normalize(links))
            COLLECTOR.add(t)
            return t
        }

        @Suppress("unused")
        @RequiresIdFrom(type = AliasFnFeLinks::class)
        fun getIntId(links: AliasFnFeLinks): Int {
            return COLLECTOR.apply(links)
        }
    }
}