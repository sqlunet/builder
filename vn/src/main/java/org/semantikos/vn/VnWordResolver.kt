package org.semantikos.vn

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.DeSerializeJsonNIDs.deserializeJson
import org.semantikos.common.Resolver
import java.io.File

typealias VnWordResolvable = String
typealias VnWordResolved = Int

class VnWordResolver(ser: String) :
    Resolver<VnWordResolvable, VnWordResolved>(if (ser.endsWith(".json")) deserializeJson<VnWordResolved>(File(ser)) else deserialize(File(ser)))
