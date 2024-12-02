package org.sqlbuilder.sl

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import org.sqlbuilder2.ser.Pair
import org.sqlbuilder2.ser.Triplet
import java.io.File

typealias VnRoleResolvable = Pair<String, String>
typealias VnRoleResolved = Triplet<Int, Int, Int>

class VnRoleResolver(ser: String) : Resolver<VnRoleResolvable, VnRoleResolved>(deserialize<Map<VnRoleResolvable, VnRoleResolved>>(File(ser)))
