package org.semantikos.pm

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.Resolver
import java.io.File

typealias PmVnRoleResolvable = Pair<String, String>
typealias PmVnRoleResolved = Triple<Int, Int, Int>

class VnRoleResolver(ser: String) : Resolver<PmVnRoleResolvable, PmVnRoleResolved>(deserialize<Map<PmVnRoleResolvable, PmVnRoleResolved>>(File(ser)))
