package org.sqlbuilder.pm.objects

import org.sqlbuilder.common.Resolvable
import org.sqlbuilder.common.Utils.nullableQuotedEscapedString
import org.sqlbuilder.pm.PmPbRoleResolvable
import org.sqlbuilder.pm.PmPbRoleResolved

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
