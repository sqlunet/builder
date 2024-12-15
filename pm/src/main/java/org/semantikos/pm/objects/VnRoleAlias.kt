package org.semantikos.pm.objects

import org.semantikos.common.Resolvable
import org.semantikos.common.Utils.nullableQuotedEscapedString
import org.semantikos.pm.PmVnRoleResolvable
import org.semantikos.pm.PmVnRoleResolved

class VnRoleAlias : Resolvable<PmVnRoleResolvable, PmVnRoleResolved> {

    var clazz: String? = null

    var role: String? = null

    override fun dataRow(): String {
        return "${nullableQuotedEscapedString(clazz)},${nullableQuotedEscapedString<String?>(role)}"
    }

    override fun resolving(): Pair<String, String> {
        return Pair(clazz!!, role!!)
    }
}
