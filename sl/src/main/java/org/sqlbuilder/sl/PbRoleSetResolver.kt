package org.sqlbuilder.sl

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import org.sqlbuilder2.ser.Pair
import java.io.File

typealias PbRoleSetResolvable = String
typealias PbRoleSetResolved = Int

class PbRoleSetResolver(ser: String) : Resolver<PbRoleSetResolvable, PbRoleSetResolved>(deserialize<Map<PbRoleSetResolvable, PbRoleSetResolved>>(File(ser)))
