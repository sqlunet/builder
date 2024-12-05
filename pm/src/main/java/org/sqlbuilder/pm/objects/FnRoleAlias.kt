package org.sqlbuilder.pm.objects

import org.sqlbuilder.common.Resolvable
import org.sqlbuilder.common.Utils.nullableQuotedEscapedString
import org.sqlbuilder.pm.PmFnRoleAndLuResolvable
import org.sqlbuilder.pm.PmFnRoleAndLuResolved

class FnRoleAlias : Resolvable<PmFnRoleAndLuResolvable, PmFnRoleAndLuResolved> {

    @JvmField
    var frame: String? = null

    @JvmField
    var fetype: String? = null

    @JvmField
    var lu: String? = null

    override fun dataRow(): String {
        return "${nullableQuotedEscapedString(frame)},${nullableQuotedEscapedString(fetype)},${nullableQuotedEscapedString(lu)}"
    }

    override fun resolving(): PmFnRoleAndLuResolvable {
        return PmFnRoleAndLuResolvable(frame!!, fetype!!, lu!!)
    }
}
