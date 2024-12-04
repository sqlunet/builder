package org.sqlbuilder.vn

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import java.io.File
import java.util.AbstractMap.SimpleEntry

typealias VnSensekeyResolvable = String
typealias VnSensekeyResolved = Pair<Int, Int>

class VnSensekeyResolver(ser: String) : Resolver<String, SimpleEntry<Int, Int>>(deserialize<MutableMap<String, out SimpleEntry<Int, Int>>>(File(ser)))

