package org.sqlbuilder.pm

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import java.io.File

typealias PmWordResolvable = String
typealias PmWordResolved = Int

class WordResolver(ser: String) : Resolver<PmWordResolvable, PmWordResolved>(deserialize<Map<PmWordResolvable, PmWordResolved>>(File(ser)))
