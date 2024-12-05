package org.sqlbuilder.fn.types

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.common.SqlId.getSqlId

object GfType {

    val COMPARATOR: Comparator<String> = Comparator.naturalOrder()

    @JvmField
    val COLLECTOR = SetCollector<String>(COMPARATOR)

    @JvmStatic
    fun add(type: String) {
        LabelType.COLLECTOR.add(type)
    }

    @RequiresIdFrom(type = GfType::class)
    fun getIntId(value: String?): Int? {
        return if (value == null) null else COLLECTOR.invoke(value)
    }

    @JvmStatic
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

