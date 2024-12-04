package org.sqlbuilder.su

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import java.io.File

typealias SuWordResolvable = String
typealias SuWordResolved = Int

class SuWordResolver(ser: String) : Resolver<SuWordResolvable, SuWordResolved>(deserialize<Map<SuWordResolvable, SuWordResolved>>(File(ser)))
