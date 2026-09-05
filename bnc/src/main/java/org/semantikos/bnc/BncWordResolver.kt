package org.semantikos.bnc

import org.semantikos.common.DeSerializeJsonNIDs.deserializeJsonMap
import org.semantikos.common.Resolver
import java.io.File

typealias BncWordResolvable = String
typealias BncWordResolved = Int

class BncWordResolver(ser: String) : Resolver<BncWordResolvable, BncWordResolved>(deserializeJsonMap<Int>(File(ser)))
