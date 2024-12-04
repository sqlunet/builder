package org.sqlbuilder.pm

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import java.io.File

typealias PmPbRoleResolvable = Pair<String, String>
typealias PmPbRoleResolved = Pair<Int, Int>

class PbRoleResolver(ser: String) : Resolver<PmPbRoleResolvable, PmPbRoleResolved>(deserialize<Map<PmPbRoleResolvable, PmPbRoleResolved>>(File(ser)))
