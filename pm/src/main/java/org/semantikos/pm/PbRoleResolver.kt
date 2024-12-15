package org.semantikos.pm

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.Resolver
import java.io.File

typealias PmPbRoleResolvable = Pair<String, String>
typealias PmPbRoleResolved = Pair<Int, Int>

class PbRoleResolver(ser: String) : Resolver<PmPbRoleResolvable, PmPbRoleResolved>(deserialize<Map<PmPbRoleResolvable, PmPbRoleResolved>>(File(ser)))
