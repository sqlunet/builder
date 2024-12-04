package org.sqlbuilder.vn

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import java.io.File
import java.util.AbstractMap.SimpleEntry

typealias VnSensekeyResolvable = String
typealias VnSensekeyResolved = SimpleEntry<Int, Int>

class VnSensekeyResolver(ser: String) : Resolver<VnSensekeyResolvable, VnSensekeyResolved>(deserialize<MutableMap<String, VnSensekeyResolved>>(File(ser)))
