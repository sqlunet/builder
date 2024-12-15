package org.semantikos.pb

import org.semantikos.common.DeSerialize
import org.semantikos.common.Resolver
import java.io.File

typealias PbFnFeResolvable = Pair<String, String>
typealias PbFnFeResolved = Triple<Int, Int, Int>

class FnFeResolver(ser: String) : Resolver<PbFnFeResolvable, PbFnFeResolved>(DeSerialize.deserialize<Map<PbFnFeResolvable, PbFnFeResolved>>(File(ser)))
