package org.semantikos.pm

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.Resolver
import java.io.File

typealias PmFnRoleResolvable = Pair<String, String>
typealias PmFnRoleResolved = Triple<Int, Int, Int>
typealias PmFnRoleAndLuResolvable = Triple<String, String, String>
typealias PmFnRoleAndLuResolved = Triple<Int, Int, Int>

class FnRoleResolver(ser: String) : Resolver<PmFnRoleResolvable, PmFnRoleResolved>(deserialize<Map<PmFnRoleResolvable, PmFnRoleResolved>>(File(ser)))
