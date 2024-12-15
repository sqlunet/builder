package org.semantikos.sn

import org.semantikos.common.DeSerialize
import org.semantikos.common.Resolver
import java.io.File

typealias SnWordResolved = Int
typealias SnSynsetResolved = Int
typealias SnSensekeyResolvable = String
typealias SnSensekeyResolved = Pair<SnWordResolved, SnSynsetResolved>

class SnSensekeyResolver(ser: String) : Resolver<SnSensekeyResolvable, SnSensekeyResolved>(DeSerialize.deserialize<Map<SnSensekeyResolvable, SnSensekeyResolved>>(File(ser)))
