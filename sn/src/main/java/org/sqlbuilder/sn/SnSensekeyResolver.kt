package org.sqlbuilder.sn

import org.sqlbuilder.common.Resolver
import org.sqlbuilder2.ser.DeSerialize
import java.io.File

typealias SnWordResolved = Int
typealias SnSynsetResolved = Int
typealias SnSensekeyResolvable = String
typealias SnSensekeyResolved = Pair<SnWordResolved, SnSynsetResolved>

class SnSensekeyResolver(ser: String) : Resolver<SnSensekeyResolvable, SnSensekeyResolved>(DeSerialize.deserialize<Map<SnSensekeyResolvable, SnSensekeyResolved>>(File(ser)))
