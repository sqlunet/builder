package org.sqlbuilder.pm

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import java.io.File

typealias PmFnRoleResolvable = Pair<String, String>
typealias PmFnRoleResolved = Triple<Int, Int, Int>

class FnRoleResolver(ser: String) : Resolver<PmFnRoleResolvable, PmFnRoleResolved>(deserialize<Map<PmFnRoleResolvable, PmFnRoleResolved>>(File(ser)))
