package org.semantikos.pm.objects

import org.semantikos.common.Resolvable
import org.semantikos.common.Utils.nullableQuotedEscapedString
import org.semantikos.pm.PmPbRoleResolvable
import org.semantikos.pm.PmPbRoleResolved

class PbRoleAlias : Resolvable<PmPbRoleResolvable, PmPbRoleResolved> {

    var roleset: String? = null

    var arg: String? = null

    override fun dataRow(): String {
        return "${nullableQuotedEscapedString(roleset)},${nullableQuotedEscapedString(arg)}"
    }

    override fun resolving(): PmPbRoleResolvable {
        return Pair(roleset!!, arg!!)
    }
}
