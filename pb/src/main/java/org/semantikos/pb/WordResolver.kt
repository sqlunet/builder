package org.semantikos.pb

import org.semantikos.common.DeSerializeJsonNIDs.deserializeJson
import org.semantikos.common.Resolver
import java.io.File

typealias PbWordResolvable = String
typealias PbWordResolved = Int

class WordResolver(ser: String) : Resolver<PbWordResolvable, PbWordResolved>(deserializeJson<Int>(File(ser)))
