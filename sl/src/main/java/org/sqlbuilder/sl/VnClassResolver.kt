package org.sqlbuilder.sl

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import java.io.File

typealias SlVnClassResolvable = String
typealias SlVnClassResolved = Int

class VnClassResolver(ser: String) : Resolver<SlVnClassResolvable, SlVnClassResolved>(deserialize<Map<SlVnClassResolvable, SlVnClassResolved>>(File(ser)))
