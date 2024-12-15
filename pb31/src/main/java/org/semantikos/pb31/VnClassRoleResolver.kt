package org.semantikos.pb31

import org.semantikos.common.DeSerialize
import org.semantikos.common.Resolver
import java.io.File

class VnClassRoleResolver(ser: String) : Resolver<Pair<String, String>, Triple<Int, Int, Int>>(DeSerialize.deserialize<Map<Pair<String, String>, Triple<Int, Int, Int>>>(File(ser)))
