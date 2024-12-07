package org.sqlbuilder.vn

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import java.io.File

typealias VnWordResolvable = String
typealias VnWordResolved = Int

class VnWordResolver(ser: String) : Resolver<VnWordResolvable, VnWordResolved>(deserialize<Map<VnWordResolvable, VnWordResolved>>(File(ser)))
