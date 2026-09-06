package org.semantikos.pm

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.DeSerializeJsonNIDs.deserializeJson
import org.semantikos.common.Resolver
import java.io.File

typealias PmVnWordResolvable = String
typealias PmVnWordResolved = Int

class VnWordResolver(ser: String) :
    Resolver<PmVnWordResolvable, PmVnWordResolved>(if (ser.endsWith(".json")) deserializeJson<PmWordResolved>(File(ser)) else deserialize(File(ser)))
