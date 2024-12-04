package org.sqlbuilder.su

import org.sqlbuilder.common.Resolver
import org.sqlbuilder2.ser.DeSerialize
import java.io.File

typealias SuSynsetResolvable = String
typealias SuSynsetResolved = Int

class SuSynsetResolver(ser: String) : Resolver<SuSynsetResolvable, SuSynsetResolved>(DeSerialize.deserialize<Map<SuSynsetResolvable, SuSynsetResolved>>(File(ser)))
