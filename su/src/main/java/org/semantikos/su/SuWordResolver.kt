package org.semantikos.su

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.DeSerializeJsonNIDs.deserializeJson
import org.semantikos.common.Resolver
import java.io.File

typealias SuWordResolvable = String
typealias SuWordResolved = Int

class SuWordResolver(ser: String) :
    Resolver<SuWordResolvable, SuWordResolved>(if (ser.endsWith(".json")) deserializeJson<SuWordResolved>(File(ser)) else deserialize(File(ser)))
