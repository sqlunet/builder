package org.semantikos.pm

import org.semantikos.common.DeSerialize.deserialize
import org.semantikos.common.Resolver
import java.io.File

typealias PmFnLexUnitResolvable = Pair<String, String>
typealias PmFnLexUnitResolved = Pair<Int, Int>

class FnLexUnitResolver(ser: String) : Resolver<PmFnLexUnitResolvable, PmFnLexUnitResolved>(deserialize<Map<PmFnLexUnitResolvable, PmFnLexUnitResolved>>(File(ser)))
