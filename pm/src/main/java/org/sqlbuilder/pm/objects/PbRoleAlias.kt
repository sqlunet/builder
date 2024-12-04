package org.sqlbuilder.pm.objects

import org.sqlbuilder.common.Resolvable
import org.sqlbuilder.common.Utils.nullableQuotedEscapedString

class PbRoleAlias : Resolvable<Pair<String, String>, Pair<Int, Int>> {

    @JvmField
    var roleset: String? = null

    @JvmField
    var arg: String? = null

    override fun dataRow(): String {
        return "${nullableQuotedEscapedString(roleset)},${nullableQuotedEscapedString(arg)}"
    }

    override fun resolving(): Pair<String, String> {
        return Pair(roleset!!, arg!!)
    }
}
