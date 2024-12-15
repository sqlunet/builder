package org.semantikos.pb

import org.semantikos.common.DeSerialize
import org.semantikos.common.Resolver
import java.io.File

typealias PbVnRoleResolvable = Pair<String, String>
typealias PbVnRoleResolved = Triple<Int, Int, Int>

class VnRoleResolver(ser: String) : Resolver<PbVnRoleResolvable, PbVnRoleResolved>(DeSerialize.deserialize<Map<PbVnRoleResolvable, PbVnRoleResolved>>(File(ser)))
