package org.sqlbuilder.pm.objects

import org.sqlbuilder.common.Resolvable
import org.sqlbuilder.common.Utils.nullableQuotedEscapedString

class FnRoleAlias : Resolvable<Triple<String, String, String>, Triple<Int, Int, Int>> {

    @JvmField
    var frame: String? = null

    @JvmField
    var fetype: String? = null

    @JvmField
    var lu: String? = null

    override fun dataRow(): String {
        return "${nullableQuotedEscapedString(frame)},${nullableQuotedEscapedString(fetype)},${nullableQuotedEscapedString(lu)}"
    }

    override fun resolving(): Triple<String, String, String> {
        return Triple(frame!!, fetype!!, lu!!)
    }
}
