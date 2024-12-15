package org.semantikos.fn.types

import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.SetCollector
import org.semantikos.common.SqlId.getSqlId

object GfType {

    val COMPARATOR: Comparator<String> = Comparator.naturalOrder()

    val COLLECTOR = SetCollector<String>(COMPARATOR)

    fun add(type: String) {
        LabelType.COLLECTOR.add(type)
    }

    @RequiresIdFrom(type = GfType::class)
    fun getIntId(value: String?): Int? {
        return if (value == null) null else COLLECTOR.invoke(value)
    }

    @RequiresIdFrom(type = GfType::class)
    fun getSqlId(value: String?): Any {
        return getSqlId(getIntId(value))
    }
}

/*
# gfid, gf
1, NULL
2, Appositive
3, Dep
4, Ext
5, Gen
6, Head
7, Obj
8, Quant
*/

