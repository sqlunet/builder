package org.semantikos.sl

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.Resolver
import java.io.File

typealias SlPbRoleSetResolvable = String
typealias SlPbRoleSetResolved = Int

class PbRoleSetResolver(ser: String) : Resolver<SlPbRoleSetResolvable, SlPbRoleSetResolved>(deserialize<Map<SlPbRoleSetResolvable, SlPbRoleSetResolved>>(File(ser)))
