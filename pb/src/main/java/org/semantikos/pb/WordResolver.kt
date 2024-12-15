package org.semantikos.pb

import org.semantikos.common.DeSerialize
import org.semantikos.common.Resolver
import java.io.File

typealias PbWordResolvable = String
typealias PbWordResolved = Int

class WordResolver(ser: String) : Resolver<PbWordResolvable, PbWordResolved>(DeSerialize.deserialize<Map<PbWordResolvable, PbWordResolved>>(File(ser)))
