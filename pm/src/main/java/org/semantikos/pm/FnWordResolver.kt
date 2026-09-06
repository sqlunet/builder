package org.semantikos.pm

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.DeSerializeJsonNIDs.deserializeJson
import org.semantikos.common.Resolver
import java.io.File

typealias PmFnWordResolvable = String
typealias PmFnWordResolved = Int

class FnWordResolver(ser: String) :
    Resolver<PmFnWordResolvable, PmFnWordResolved>(if (ser.endsWith(".json")) deserializeJson<PmFnWordResolved>(File(ser)) else deserialize(File(ser)))
