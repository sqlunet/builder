package org.semantikos.pb31

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.DeSerializeJsonNIDs.deserializeJson
import org.semantikos.common.Resolver
import java.io.File

typealias PbWordResolvable = String
typealias PbWordResolved = Int

class WordResolver(ser: String) :
    Resolver<PbWordResolvable, PbWordResolved>(if (ser.endsWith(".json")) deserializeJson<PbWordResolved>(File(ser)) else deserialize(File(ser)))
