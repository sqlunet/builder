package org.semantikos.pm

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.Resolver
import java.io.File

typealias PmWordResolvable = String
typealias PmWordResolved = Int

class WordResolver(ser: String) : Resolver<PmWordResolvable, PmWordResolved>(deserialize<Map<PmWordResolvable, PmWordResolved>>(File(ser)))
