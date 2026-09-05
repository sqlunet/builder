package org.semantikos.pb31

import org.semantikos.common.DeSerializeJsonNIDs.deserializeJson
import org.semantikos.common.Resolver
import java.io.File

class WordResolver(ser: String) : Resolver<String, Int>(deserializeJson<Int>(File(ser)))
