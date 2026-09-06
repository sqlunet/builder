package org.semantikos.bnc

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.DeSerializeJsonNIDs.deserializeJson
import org.semantikos.common.Resolver
import java.io.File

typealias BncWordResolvable = String
typealias BncWordResolved = Int

class BncWordResolver(ser: String) :
    Resolver<BncWordResolvable, BncWordResolved>(if (ser.endsWith(".json")) deserializeJson<BncWordResolved>(File(ser)) else deserialize(File(ser)))
