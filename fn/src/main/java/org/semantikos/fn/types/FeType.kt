package org.semantikos.fn.types

import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.SetCollector
import org.semantikos.common.SqlId.getSqlId

object FeType {

    val COMPARATOR: Comparator<String> = Comparator { obj, str -> obj.compareTo(str, ignoreCase = true) }

    val COLLECTOR = SetCollector<String>(COMPARATOR)

    @RequiresIdFrom(type = FeType::class)
    fun getIntId(value: String?): Int? {
        return if (value == null) null else COLLECTOR.invoke(value)
    }

    @RequiresIdFrom(type = FeType::class)
    fun getSqlId(value: String?): Any {
        return getSqlId(getIntId(value))
    }
}
/*
# fetypeid, fetype
1, Abundant_entities
2, Abuser
3, Accessibility
4, Accessory
5, Accoutrement
6, Accuracy
7, Accused
8, Act
9, Action
 */

