package org.sqlbuilder.sl

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import org.sqlbuilder2.ser.Pair
import org.sqlbuilder2.ser.Triplet
import java.io.File

typealias VnClassResolvable = String
typealias VnClassResolved = Int

class VnClassResolver(ser: String) : Resolver<VnClassResolvable, VnClassResolved>(deserialize<Map<VnClassResolvable, VnClassResolved>>(File(ser)))
