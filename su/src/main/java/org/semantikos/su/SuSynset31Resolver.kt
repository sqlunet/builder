package org.semantikos.su

import org.semantikos.common.DeSerialize
import org.semantikos.common.Resolver2
import java.io.File

class SuSynset31Resolver(ser: String) : Resolver2<Char, Long, Long>(DeSerialize.deserialize<Map<Char, Map<Long, Long>>>(File(ser)))

