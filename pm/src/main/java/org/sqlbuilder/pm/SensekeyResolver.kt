package org.sqlbuilder.pm

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import java.io.File
import java.util.AbstractMap.SimpleEntry

class SensekeyResolver(ser: String) : Resolver<String, SimpleEntry<Int, Int>>(deserialize<Map<String, SimpleEntry<Int, Int>>>(File(ser)))
