package org.semantikos.legacy

import org.semantikos.common.DeSerialize.deserialize
import java.io.File

typealias LegacyWord = String
typealias LegacyPos = Char
typealias LegacyOffset = Int
typealias LegacySensekey = String
typealias LegacyLemmaPosOffsetResolvable = Triple<LegacyWord, LegacyPos, LegacyOffset>
typealias LegacyLemmaPosOffsetResolved = LegacySensekey

class LegacyLemmaPosOffsetResolver(
    val map: Map<LegacyLemmaPosOffsetResolvable, LegacyLemmaPosOffsetResolved>,
) {
    constructor(ser: String) : this(deserialize(File(ser)) as Map<LegacyLemmaPosOffsetResolvable, LegacyLemmaPosOffsetResolved>)
}
