package org.semantikos.sn

import org.semantikos.common.DeSerialize
import org.semantikos.common.DeSerializeJsonNIDs.deserializeJson
import org.semantikos.common.Resolver
import java.io.File

typealias SnSensekeyResolvable = String
typealias SnSensekeyResolved = List<Int>

class SnSensekeyResolver(ser: String) :
    Resolver<SnSensekeyResolvable, SnSensekeyResolved>(if (ser.endsWith(".json")) deserializeJson<SnSensekeyResolved>(File(ser)) else DeSerialize.deserialize(File(ser)))
