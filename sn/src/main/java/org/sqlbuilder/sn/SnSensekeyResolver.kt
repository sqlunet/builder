package org.sqlbuilder.sn

import org.sqlbuilder.common.Resolver
import org.sqlbuilder2.ser.DeSerialize
import java.io.File
import java.util.AbstractMap.SimpleEntry

class SnSensekeyResolver(ser: String) : Resolver<String, SimpleEntry<Int, Int>>(DeSerialize.deserialize<MutableMap<String, out SimpleEntry<Int, Int>>>(File(ser)))
