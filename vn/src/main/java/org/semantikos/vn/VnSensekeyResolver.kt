package org.semantikos.vn

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.DeSerializeJsonNIDs.deserializeJson
import org.semantikos.common.Resolver
import java.io.File

typealias VnSensekeyResolvable = String
typealias VnSensekeyResolved = List<Int>

class VnSensekeyResolver(ser: String) :
    Resolver<VnSensekeyResolvable, VnSensekeyResolved>(if (ser.endsWith(".json")) deserializeJson<VnSensekeyResolved>(File(ser)) else deserialize(File(ser)))
