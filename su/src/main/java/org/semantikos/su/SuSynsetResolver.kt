package org.semantikos.su

import org.semantikos.common.DeSerialize
import org.semantikos.common.Resolver
import java.io.File

typealias SuSynsetResolvable = String
typealias SuSynsetResolved = Int

class SuSynsetResolver(ser: String) : Resolver<SuSynsetResolvable, SuSynsetResolved>(DeSerialize.deserialize<Map<SuSynsetResolvable, SuSynsetResolved>>(File(ser)))
