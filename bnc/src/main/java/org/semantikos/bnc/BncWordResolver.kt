package org.semantikos.bnc

import org.semantikos.common.DeSerialize
import org.semantikos.common.Resolver
import java.io.File

typealias BncWordResolvable = String
typealias BncWordResolved = Int

class BncWordResolver(ser: String) : Resolver<BncWordResolvable, BncWordResolved>(DeSerialize.deserialize<Map<BncWordResolvable, BncWordResolved>>(File(ser)))