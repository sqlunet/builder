package org.semantikos.sn

import org.semantikos.common.DeSerialize
import org.semantikos.common.Resolver
import java.io.File

typealias SnWord =  String
typealias SnPos =  Char
typealias SnOffset = Int
typealias SnLemmaPosOffsetResolvable = Triple<SnWord, SnPos, SnOffset>
typealias SnLemmaPosOffsetResolved = SnSensekeyResolvable

class SnLemmaPosOffsetResolver(ser: String) : Resolver<SnLemmaPosOffsetResolvable, SnLemmaPosOffsetResolved>(DeSerialize.deserialize<Map<SnLemmaPosOffsetResolvable, SnLemmaPosOffsetResolved>>(File(ser)))
