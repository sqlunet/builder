package org.sqlbuilder.sl

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import java.io.File

typealias SlPbRoleSetResolvable = String
typealias SlPbRoleSetResolved = Int

class PbRoleSetResolver(ser: String) : Resolver<SlPbRoleSetResolvable, SlPbRoleSetResolved>(deserialize<Map<SlPbRoleSetResolvable, SlPbRoleSetResolved>>(File(ser)))
