package org.sqlbuilder.su

import org.sqlbuilder.common.Resolver
import org.sqlbuilder2.ser.DeSerialize
import java.io.File

class SuSynsetResolver(ser: String) : Resolver<String, Int>(DeSerialize.deserialize<Map<String, Int>>(File(ser)))

