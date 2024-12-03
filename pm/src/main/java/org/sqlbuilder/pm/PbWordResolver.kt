package org.sqlbuilder.pm

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import java.io.File

class PbWordResolver(ser: String) : Resolver<String, Int>(deserialize<Map<String, Int>>(File(ser)))
