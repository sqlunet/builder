package org.sqlbuilder.pb

import org.sqlbuilder.common.DeSerialize
import org.sqlbuilder.common.Resolver
import org.sqlbuilder2.ser.Pair
import org.sqlbuilder2.ser.Triplet
import java.io.File

class FnFrameFeResolver(ser: String) : Resolver<Pair<String?, String?>, Triplet<Int?, Int?, Int?>>(DeSerialize.deserialize<Map<Pair<String?, String?>, Triplet<Int?, Int?, Int?>>>(File(ser)))
