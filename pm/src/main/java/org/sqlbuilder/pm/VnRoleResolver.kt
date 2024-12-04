package org.sqlbuilder.pm

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import java.io.File

typealias PmVnRoleResolvable = Pair<String, String>
typealias PmVnRoleResolved = Triple<Int, Int, Int>

class VnRoleResolver(ser: String) : Resolver<PmVnRoleResolvable, PmVnRoleResolved>(deserialize<Map<PmVnRoleResolvable, PmVnRoleResolved>>(File(ser)))
