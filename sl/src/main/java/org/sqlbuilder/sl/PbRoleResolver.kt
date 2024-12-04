package org.sqlbuilder.sl

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import java.io.File

typealias SlPbRoleResolvable = Pair<String, String>
typealias SlPbRoleResolved = Pair<Int, Int>

class PbRoleResolver(ser: String) : Resolver<SlPbRoleResolvable, SlPbRoleResolved>(deserialize<Map<SlPbRoleResolvable, SlPbRoleResolved>>(File(ser)))
