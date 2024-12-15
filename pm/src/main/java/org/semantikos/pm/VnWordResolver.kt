package org.semantikos.pm

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.Resolver
import java.io.File

typealias PmVnWordResolvable = String
typealias PmVnWordResolved = Int

class VnWordResolver(ser: String) : Resolver<PmVnWordResolvable, PmVnWordResolved>(deserialize<Map<PmVnWordResolvable, PmVnWordResolved>>(File(ser)))
