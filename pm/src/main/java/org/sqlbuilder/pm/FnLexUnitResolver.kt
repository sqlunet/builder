package org.sqlbuilder.pm

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import java.io.File

class FnLexUnitResolver(ser: String) : Resolver<Pair<String, String>, Pair<Int, Int>>(deserialize<Map<Pair<String, String>, Pair<Int, Int>>>(File(ser)))
