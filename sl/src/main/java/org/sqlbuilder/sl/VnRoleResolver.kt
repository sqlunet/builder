package org.sqlbuilder.sl

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import java.io.File

typealias VnRoleResolvable = Pair<String, String>
typealias VnRoleResolved = Triple<Int, Int, Int>

class VnRoleResolver(ser: String) : Resolver<VnRoleResolvable, VnRoleResolved>(deserialize<Map<VnRoleResolvable, VnRoleResolved>>(File(ser)))
