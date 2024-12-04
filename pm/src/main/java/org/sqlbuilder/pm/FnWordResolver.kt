package org.sqlbuilder.pm

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import java.io.File

typealias PmFnWordResolvable = String
typealias PmFnWordResolved = Int

class FnWordResolver(ser: String) : Resolver<PmFnWordResolvable, PmFnWordResolved>(deserialize<Map<PmFnWordResolvable, PmFnWordResolved>>(File(ser)))
