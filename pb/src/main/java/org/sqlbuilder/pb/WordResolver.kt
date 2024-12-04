package org.sqlbuilder.pb

import org.sqlbuilder.common.DeSerialize
import org.sqlbuilder.common.Resolver
import java.io.File

typealias PbWordResolvable = String
typealias PbWordResolved = Int

class WordResolver(ser: String) : Resolver<PbWordResolvable, PbWordResolved>(DeSerialize.deserialize<Map<PbWordResolvable, PbWordResolved>>(File(ser)))
