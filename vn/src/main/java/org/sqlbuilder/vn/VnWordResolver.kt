package org.sqlbuilder.vn

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import java.io.File

class VnWordResolver(ser: String) : Resolver<String, Int>(deserialize<MutableMap<String, out Int>>(File(ser)))

