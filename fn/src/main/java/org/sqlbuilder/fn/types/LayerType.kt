package org.sqlbuilder.fn.types

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.common.SqlId.getSqlId

object LayerType {

    val COMPARATOR: Comparator<String> = Comparator.naturalOrder()

    @JvmField
    val COLLECTOR = SetCollector<String>(COMPARATOR)

    @JvmStatic
    fun add(type: String) {
        COLLECTOR.add(type)
    }

    @JvmStatic
    @RequiresIdFrom(type = LayerType::class)
    fun getIntId(value: String?): Int? {
        return if (value == null) null else COLLECTOR.invoke(value)
    }

    @RequiresIdFrom(type = LayerType::class)
    fun getSqlId(value: String?): Any {
        return getSqlId(getIntId(value))
    }
}

/*
# layertypeid, layertype
1, Adj
2, Adv
3, Art
4, BNC
5, CE
6, CEE
7, CstrPT
8, FE
9, GF
10, GovX
11, NER
12, Noun
13, Other
14, PENN
15, Prep
16, PT
17, Scon
18, Sent
19, Target
20, Verb
21, WSL
 */

