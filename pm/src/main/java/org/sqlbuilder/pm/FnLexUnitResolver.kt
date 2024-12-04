package org.sqlbuilder.pm

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import java.io.File

typealias PmFnLexUnitResolvable = Pair<String, String>
typealias PmFnLexUnitResolved = Pair<Int, Int>

class FnLexUnitResolver(ser: String) : Resolver<PmFnLexUnitResolvable, PmFnLexUnitResolved>(deserialize<Map<PmFnLexUnitResolvable, PmFnLexUnitResolved>>(File(ser)))
