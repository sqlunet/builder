package org.semantikos.pm

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.Resolver
import java.io.File

typealias PmFnWordResolvable = String
typealias PmFnWordResolved = Int

class FnWordResolver(ser: String) : Resolver<PmFnWordResolvable, PmFnWordResolved>(deserialize<Map<PmFnWordResolvable, PmFnWordResolved>>(File(ser)))
