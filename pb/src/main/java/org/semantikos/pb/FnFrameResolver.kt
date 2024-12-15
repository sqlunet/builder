package org.semantikos.pb

import org.semantikos.common.DeSerialize
import org.semantikos.common.Resolver
import java.io.File

typealias PbFnFrameResolvable = String
typealias PbFnFrameResolved = Int

class FnFrameResolver(ser: String) : Resolver<PbFnFrameResolvable, PbFnFrameResolved>(DeSerialize.deserialize<Map<PbFnFrameResolvable, PbFnFrameResolved>>(File(ser)))
