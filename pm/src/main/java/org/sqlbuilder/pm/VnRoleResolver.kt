package org.sqlbuilder.pm

import org.sqlbuilder.common.DeSerialize.deserialize
import org.sqlbuilder.common.Resolver
import org.sqlbuilder2.ser.Pair
import org.sqlbuilder2.ser.Triplet
import java.io.File

class VnRoleResolver(ser: String) : Resolver<Pair<String, String>, Triplet<Int, Int, Int>>(deserialize<Map<Pair<String, String>, Triplet<Int, Int, Int>>>(File(ser)))
