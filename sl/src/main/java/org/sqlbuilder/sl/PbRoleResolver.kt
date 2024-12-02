package org.sqlbuilder.sl

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import org.sqlbuilder2.ser.Pair
import java.io.File

typealias PbRoleResolvable = Pair<String, String>
typealias PbRoleResolved = Pair<Int, Int>

class PbRoleResolver(ser: String) : Resolver<PbRoleResolvable, PbRoleResolved>(deserialize<Map<PbRoleResolvable, PbRoleResolved>>(File(ser)))
