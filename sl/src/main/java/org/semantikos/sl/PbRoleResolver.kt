package org.semantikos.sl

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.Resolver
import java.io.File

typealias SlPbRoleResolvable = Pair<String, String>
typealias SlPbRoleResolved = Pair<Int, Int>

class PbRoleResolver(ser: String) : Resolver<SlPbRoleResolvable, SlPbRoleResolved>(deserialize<Map<SlPbRoleResolvable, SlPbRoleResolved>>(File(ser)))
