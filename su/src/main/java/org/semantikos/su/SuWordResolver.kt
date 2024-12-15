package org.semantikos.su

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.Resolver
import java.io.File

typealias SuWordResolvable = String
typealias SuWordResolved = Int

class SuWordResolver(ser: String) : Resolver<SuWordResolvable, SuWordResolved>(deserialize<Map<SuWordResolvable, SuWordResolved>>(File(ser)))
