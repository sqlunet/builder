package org.semantikos.pm

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.Resolver
import java.io.File

typealias PmSensekeyResolvable = String
typealias PmSensekeyResolved = Pair<Int, Int>

class SensekeyResolver(ser: String) : Resolver<PmSensekeyResolvable, PmSensekeyResolved>(deserialize<Map<PmSensekeyResolvable, PmSensekeyResolved>>(File(ser)))
