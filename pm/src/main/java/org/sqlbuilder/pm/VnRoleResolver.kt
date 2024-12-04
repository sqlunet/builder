package org.sqlbuilder.pm

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import java.io.File

class VnRoleResolver(ser: String) : Resolver<Pair<String, String>, Triple<Int, Int, Int>>(deserialize<Map<Pair<String, String>, Triple<Int, Int, Int>>>(File(ser)))
