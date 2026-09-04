package org.semantikos.pb

import org.semantikos.common.DeSerializeJsonNIDs.deserializeJsonMap
import org.semantikos.common.Resolver
import java.io.File

typealias PbWordResolvable = String
typealias PbWordResolved = Int

class WordResolver(ser: String) : Resolver<PbWordResolvable, PbWordResolved>(deserializeJsonMap(File(ser)))
