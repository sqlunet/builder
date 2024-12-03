package org.sqlbuilder.pm.objects

import org.sqlbuilder.common.Resolvable
import org.sqlbuilder.common.Utils.nullableQuotedEscapedString
import org.sqlbuilder2.ser.Triplet

class FnRoleAlias : Resolvable<Triplet<String, String, String>, Triplet<Int, Int, Int>> {

    @JvmField
    var frame: String? = null

    @JvmField
    var fetype: String? = null

    @JvmField
    var lu: String? = null

    override fun dataRow(): String {
        return "${nullableQuotedEscapedString(frame)},${nullableQuotedEscapedString(fetype)},${nullableQuotedEscapedString(lu)}"
    }

    override fun resolving(): Triplet<String, String, String> {
        return Triplet(frame!!, fetype!!, lu!!)
    }
}
