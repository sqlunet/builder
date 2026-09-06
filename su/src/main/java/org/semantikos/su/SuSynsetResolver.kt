package org.semantikos.su

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.DeSerializeJsonNIDs.deserializeJson
import org.semantikos.common.Resolver
import java.io.File

typealias SuSynsetResolvable = String
typealias SuSynsetResolved = Int

class SuSynsetResolver(ser: String) :
    Resolver<SuSynsetResolvable, SuSynsetResolved>(if (ser.endsWith(".json")) deserializeJson<SuWordResolved>(File(ser)) else deserialize(File(ser)))
