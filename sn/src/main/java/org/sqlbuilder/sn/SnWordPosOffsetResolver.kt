package org.sqlbuilder.sn

import org.sqlbuilder.common.Resolver
import org.sqlbuilder2.ser.DeSerialize
import java.io.File

typealias SnWord =  String
typealias SnPos =  Char
typealias SnOffset = Int
typealias SnWordPosOffsetResolvable = Triple<SnWord, SnPos, SnOffset>
typealias SnWordPosOffsetResolved = SnSensekeyResolvable

class SnWordPosOffsetResolver(ser: String) : Resolver<SnWordPosOffsetResolvable, SnWordPosOffsetResolved>(DeSerialize.deserialize<Map<SnWordPosOffsetResolvable, SnWordPosOffsetResolved>>(File(ser)))
