package org.sqlbuilder.fn

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import java.io.File

typealias FnWordResolvable = String
typealias FnWordResolved = Int

class FnWordResolver(ser: String) : Resolver<FnWordResolvable, FnWordResolved>(deserialize<Map<FnWordResolvable, FnWordResolved>>(File(ser)))
