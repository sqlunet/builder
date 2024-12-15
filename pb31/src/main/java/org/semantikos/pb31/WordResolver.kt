package org.semantikos.pb31

import org.semantikos.common.DeSerialize
import org.semantikos.common.Resolver
import java.io.File

class WordResolver(ser: String) : Resolver<String, Int>(DeSerialize.deserialize<Map<String, Int>>(File(ser)))

