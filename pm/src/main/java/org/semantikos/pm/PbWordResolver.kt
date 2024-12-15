package org.semantikos.pm

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.Resolver
import java.io.File

typealias PmPbWordResolvable = String
typealias PmPbWordResolved = Int

class PbWordResolver(ser: String) : Resolver<PmPbWordResolvable, PmPbWordResolved>(deserialize<Map<PmPbWordResolvable, PmPbWordResolved>>(File(ser)))
