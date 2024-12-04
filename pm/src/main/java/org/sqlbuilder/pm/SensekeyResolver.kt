package org.sqlbuilder.pm

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import java.io.File

typealias PmSensekeyResolvable = String
typealias PmSensekeyResolved = Pair<Int, Int>

class SensekeyResolver(ser: String) : Resolver<PmSensekeyResolvable, PmSensekeyResolved>(deserialize<Map<PmSensekeyResolvable, PmSensekeyResolved>>(File(ser)))
