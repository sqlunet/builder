package org.sqlbuilder.su

import org.sqlbuilder.common.Resolver2
import org.sqlbuilder2.ser.DeSerialize
import java.io.File

class SuSynset31Resolver(ser: String) : Resolver2<Char, Long, Long>(DeSerialize.deserialize<Map<Char, Map<Long, Long>>>(File(ser)))

