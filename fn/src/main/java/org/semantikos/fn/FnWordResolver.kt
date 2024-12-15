package org.semantikos.fn

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.Resolver
import java.io.File

typealias FnWordResolvable = String
typealias FnWordResolved = Int

class FnWordResolver(ser: String) : Resolver<FnWordResolvable, FnWordResolved>(deserialize<Map<FnWordResolvable, FnWordResolved>>(File(ser)))
