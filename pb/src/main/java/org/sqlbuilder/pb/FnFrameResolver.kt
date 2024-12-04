package org.sqlbuilder.pb

import org.sqlbuilder.common.DeSerialize
import org.sqlbuilder.common.Resolver
import java.io.File

typealias PbFnFrameResolvable = String
typealias PbFnFrameResolved = Int

class FnFrameResolver(ser: String) : Resolver<PbFnFrameResolvable, PbFnFrameResolved>(DeSerialize.deserialize<Map<PbFnFrameResolvable, PbFnFrameResolved>>(File(ser)))
