package org.semantikos.vn

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.Resolver
import java.io.File

typealias VnSensekeyResolvable = String?
typealias VnSensekeyResolved = Pair<Int, Int>

class VnSensekeyResolver(ser: String) : Resolver<VnSensekeyResolvable, VnSensekeyResolved>(deserialize<Map<VnSensekeyResolvable, VnSensekeyResolved>>(File(ser)))
