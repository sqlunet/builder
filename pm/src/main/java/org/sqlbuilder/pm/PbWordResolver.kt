package org.sqlbuilder.pm

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import java.io.File

typealias PmPbWordResolvable = String
typealias PmPbWordResolved = Int

class PbWordResolver(ser: String) : Resolver<PmPbWordResolvable, PmPbWordResolved>(deserialize<Map<PmPbWordResolvable, PmPbWordResolved>>(File(ser)))
