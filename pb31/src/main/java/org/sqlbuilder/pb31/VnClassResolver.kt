package org.sqlbuilder.pb31

import org.sqlbuilder.common.DeSerialize
import org.sqlbuilder.common.Resolver
import java.io.File

class VnClassResolver(ser: String) : Resolver<String, Int>(DeSerialize.deserialize<Map<String, Int>>(File(ser)))
