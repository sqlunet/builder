package org.sqlbuilder.pm

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import java.io.File

typealias PmVnWordResolvable = String
typealias PmVnWordResolved = Int

class VnWordResolver(ser: String) : Resolver<PmVnWordResolvable, PmVnWordResolved>(deserialize<Map<PmVnWordResolvable, PmVnWordResolved>>(File(ser)))
