package org.sqlbuilder.pm.objects

import org.sqlbuilder.common.Resolvable
import org.sqlbuilder.common.Utils.nullableQuotedEscapedString
import org.sqlbuilder2.ser.Pair

class VnRoleAlias : Resolvable<Pair<String, String>, Pair<Int, Int>> {

    @JvmField
    var clazz: String? = null

    @JvmField
    var role: String? = null

    override fun dataRow(): String {
        return "${nullableQuotedEscapedString(clazz)},${nullableQuotedEscapedString<String?>(role)}"
    }

    override fun resolving(): Pair<String, String> {
        return Pair(clazz!!, role!!)
    }
}
