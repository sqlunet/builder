package org.sqlbuilder.sn

import org.sqlbuilder.common.Resolver
import org.sqlbuilder2.ser.DeSerialize
import java.io.File

typealias SnSensekeyResolvable = String
typealias SnSensekeyResolved = Pair<Int, Int>

class SnSensekeyResolver(ser: String) : Resolver<SnSensekeyResolvable, SnSensekeyResolved>(DeSerialize.deserialize<Map<SnSensekeyResolvable, SnSensekeyResolved>>(File(ser)))
