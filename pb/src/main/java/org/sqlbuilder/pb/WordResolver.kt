package org.sqlbuilder.pb

import org.sqlbuilder.common.DeSerialize
import org.sqlbuilder.common.Resolver
import java.io.File

class WordResolver(ser: String) : Resolver<String, Int>(DeSerialize.deserialize<Map<String, Int>?>(File(ser)))

