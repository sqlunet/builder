package org.semantikos.pb.foreign

import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.SetCollector

class AliasVnRoleLinks private constructor(
    links: Collection<String>,
) : AliasRoleLinks(links) {

    // N I D

    @RequiresIdFrom(type = AliasVnRoleLinks::class)
    override fun getIntId(): Int {
        return COLLECTOR.invoke(this)
    }

    companion object {

        val COLLECTOR = SetCollector<AliasRoleLinks>(COMPARATOR)

        fun make(links: Collection<String>): AliasVnRoleLinks {
            val t = AliasVnRoleLinks(normalize(links))
            COLLECTOR.add(t)
            return t
        }

        @Suppress("unused")
        @RequiresIdFrom(type = AliasVnRoleLinks::class)
        fun getIntId(links: AliasVnRoleLinks): Int {
            return COLLECTOR.invoke(links)
        }
    }
}