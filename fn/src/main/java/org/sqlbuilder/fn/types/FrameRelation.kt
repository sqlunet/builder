package org.sqlbuilder.fn.types

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.common.SqlId.getSqlId

object FrameRelation {

    val COMPARATOR: Comparator<String> = Comparator.naturalOrder()

    val COLLECTOR = SetCollector<String>(COMPARATOR)

    fun add(type: String) {
        COLLECTOR.add(type)
    }

    @RequiresIdFrom(type = FrameRelation::class)
    fun getIntId(value: String?): Int? {
        return if (value == null) null else COLLECTOR.invoke(value)
    }

    @RequiresIdFrom(type = FrameRelation::class)
    fun getSqlId(value: String?): Any {
        return getSqlId(getIntId(value))
    }
}

/*
# relationid, relation
1, Has Subframe(s)
2, Inherits from
3, Is Causative of
4, Is Inchoative of
5, Is Inherited by
6, Is Perspectivized in
7, Is Preceded by
8, Is Used by
9, Perspective on
10, Precedes
11, See also
12, Subframe of
13, Uses
*/

