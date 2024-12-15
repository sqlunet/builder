package org.semantikos.sl

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.Resolver
import java.io.File

typealias SlVnClassResolvable = String
typealias SlVnClassResolved = Int

class VnClassResolver(ser: String) : Resolver<SlVnClassResolvable, SlVnClassResolved>(deserialize<Map<SlVnClassResolvable, SlVnClassResolved>>(File(ser)))
