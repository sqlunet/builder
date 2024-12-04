package org.sqlbuilder.sl

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import java.io.File

typealias SlVnRoleResolvable = Pair<String, String>
typealias SlVnRoleResolved = Triple<Int, Int, Int>

class VnRoleResolver(ser: String) : Resolver<SlVnRoleResolvable, SlVnRoleResolved>(deserialize<Map<SlVnRoleResolvable, SlVnRoleResolved>>(File(ser)))
