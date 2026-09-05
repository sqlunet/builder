package org.semantikos.pm

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.DeSerializeJsonNIDs.deserializeJson
import org.semantikos.common.Resolver
import java.io.File

typealias PmSensekeyResolvable = String
typealias PmSensekeyResolved = List<Int>

class SensekeyResolver(val ser: String) :
    Resolver<PmSensekeyResolvable, PmSensekeyResolved>(if (ser.endsWith(".json")) deserializeJson<PmSensekeyResolved>(File(ser)) else deserialize(File(ser)))
