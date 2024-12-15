package org.semantikos.sl

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.Resolver
import java.io.File

typealias SlVnRoleResolvable = Pair<String, String>
typealias SlVnRoleResolved = Triple<Int, Int, Int>

class VnRoleResolver(ser: String) : Resolver<SlVnRoleResolvable, SlVnRoleResolved>(deserialize<Map<SlVnRoleResolvable, SlVnRoleResolved>>(File(ser)))
