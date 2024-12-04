package org.sqlbuilder.pb

import org.sqlbuilder.common.DeSerialize
import org.sqlbuilder.common.Resolver
import java.io.File

class FnFrameFeResolver(ser: String) : Resolver<Pair<String, String>, Triple<Int, Int, Int>>(DeSerialize.deserialize<Map<Pair<String, String>, Triple<Int, Int, Int>>>(File(ser)))
