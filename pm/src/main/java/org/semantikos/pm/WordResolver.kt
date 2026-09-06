package org.semantikos.pm

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.DeSerializeJsonNIDs.deserializeJson
import org.semantikos.common.Resolver
import java.io.File

typealias PmWordResolvable = String
typealias PmWordResolved = Int

class WordResolver(ser: String) :
    Resolver<PmWordResolvable, PmWordResolved>(if (ser.endsWith(".json")) deserializeJson<PmWordResolved>(File(ser)) else deserialize(File(ser)))
