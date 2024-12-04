package org.sqlbuilder.bnc

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import java.io.File

typealias BncWordResolvable = String
typealias BncWordResolved = Int

class BncWordResolver(ser: String) : Resolver<BncWordResolvable, BncWordResolved>(deserialize<Map<BncWordResolvable, BncWordResolved>>(File(ser)))
