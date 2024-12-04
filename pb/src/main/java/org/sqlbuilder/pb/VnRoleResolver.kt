package org.sqlbuilder.pb

import org.sqlbuilder.common.DeSerialize
import org.sqlbuilder.common.Resolver
import java.io.File

typealias PbVnRoleResolvable = Pair<String, String>
typealias PbVnRoleResolved = Triple<Int, Int, Int>

class VnRoleResolver(ser: String) : Resolver<PbVnRoleResolvable, PbVnRoleResolved>(DeSerialize.deserialize<Map<PbVnRoleResolvable, PbVnRoleResolved>>(File(ser)))
