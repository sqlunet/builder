package org.semantikos.vn

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.Resolver
import java.io.File

typealias VnWordResolvable = String
typealias VnWordResolved = Int

class VnWordResolver(ser: String) : Resolver<VnWordResolvable, VnWordResolved>(deserialize<Map<VnWordResolvable, VnWordResolved>>(File(ser)))
