package org.sqlbuilder.pb

import org.sqlbuilder.common.DeSerialize
import org.sqlbuilder.common.Resolver
import java.io.File

typealias PbVnClassResolvable = String
typealias PbVnClassResolved = Int

class VnClassResolver(ser: String) : Resolver<PbVnClassResolvable, PbVnClassResolved>(DeSerialize.deserialize<Map<PbVnClassResolvable, PbVnClassResolved>>(File(ser)))
