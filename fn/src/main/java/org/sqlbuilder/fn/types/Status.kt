package org.sqlbuilder.fn.types

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.common.SqlId.getSqlId

object Status {

    val COMPARATOR: Comparator<String> = Comparator.naturalOrder()

    val COLLECTOR = SetCollector<String>(COMPARATOR)

    @JvmStatic
    fun add(value: String) {
        COLLECTOR.add(value)
    }

    @RequiresIdFrom(type = Status::class)
    fun getIntId(value: String?): Int? {
        return if (value == null) null else COLLECTOR.apply(value)
    }

    @RequiresIdFrom(type = Status::class)
    fun getSqlId(value: String?): Any {
        return getSqlId(getIntId(value))
    }
}

/*
# statusid, status
1, Add_Annotation
2, BTDT
3, Created
4, Finished_Initial
5, Finished_X-Gov
6, FN1_Sent
7, Insufficient_Attestations
8, In_Use
9, Needs_SCs
10, New
11, Pre-Marked
12, Rules_Defined
13, AUTO_APP
14, AUTO_EDITED
15, MANUAL
16, UNANN
*/

