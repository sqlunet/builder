package org.semantikos.pb

import org.semantikos.common.DeSerialize
import org.semantikos.common.Resolver
import java.io.File

typealias PbVnClassResolvable = String
typealias PbVnClassResolved = Int

class VnClassResolver(ser: String) : Resolver<PbVnClassResolvable, PbVnClassResolved>(DeSerialize.deserialize<Map<PbVnClassResolvable, PbVnClassResolved>>(File(ser)))
