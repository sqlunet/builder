package org.semantikos.pb.foreign

import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.SetCollector

class AliasFnFeLinks private constructor(
    links: Collection<String>,
) : AliasRoleLinks(links) {

    // N I D

    @RequiresIdFrom(type = AliasFnFeLinks::class)
    override fun getIntId(): Int {
        return COLLECTOR.invoke(this)
    }

    companion object {

        val COLLECTOR = SetCollector<AliasRoleLinks>(COMPARATOR)

        fun make(links: Collection<String>): AliasFnFeLinks {
            val t = AliasFnFeLinks(normalize(links))
            COLLECTOR.add(t)
            return t
        }

        @Suppress("unused")
        @RequiresIdFrom(type = AliasFnFeLinks::class)
        fun getIntId(links: AliasFnFeLinks): Int {
            return COLLECTOR.invoke(links)
        }
    }
}