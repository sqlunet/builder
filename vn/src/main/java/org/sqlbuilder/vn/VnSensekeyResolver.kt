package org.sqlbuilder.vn

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import java.io.File

typealias VnSensekeyResolvable = String
typealias VnSensekeyResolved = Pair<Int, Int>

class VnSensekeyResolver(ser: String) : Resolver<VnSensekeyResolvable, VnSensekeyResolved>(deserialize<MutableMap<String, VnSensekeyResolved>>(File(ser)))
