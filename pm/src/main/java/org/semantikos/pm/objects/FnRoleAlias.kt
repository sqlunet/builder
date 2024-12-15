package org.semantikos.pm.objects

import org.semantikos.common.Resolvable
import org.semantikos.common.Utils.nullableQuotedEscapedString
import org.semantikos.pm.PmFnRoleAndLuResolvable
import org.semantikos.pm.PmFnRoleAndLuResolved

class FnRoleAlias : Resolvable<PmFnRoleAndLuResolvable, PmFnRoleAndLuResolved> {

    var frame: String? = null

    var fetype: String? = null

    var lu: String? = null

    override fun dataRow(): String {
        return "${nullableQuotedEscapedString(frame)},${nullableQuotedEscapedString(fetype)},${nullableQuotedEscapedString(lu)}"
    }

    override fun resolving(): PmFnRoleAndLuResolvable {
        return PmFnRoleAndLuResolvable(frame!!, fetype!!, lu!!)
    }
}
