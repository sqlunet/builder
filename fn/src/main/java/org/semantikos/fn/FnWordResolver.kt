package org.semantikos.fn

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.DeSerializeJsonNIDs.deserializeJson
import org.semantikos.common.Resolver
import java.io.File

typealias FnWordResolvable = String
typealias FnWordResolved = Int

class FnWordResolver(ser: String) :
    Resolver<FnWordResolvable, FnWordResolved>(if (ser.endsWith(".json")) deserializeJson<FnWordResolved>(File(ser)) else deserialize(File(ser)))
