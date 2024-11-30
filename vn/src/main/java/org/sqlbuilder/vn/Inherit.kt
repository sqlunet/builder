package org.sqlbuilder.vn

import org.sqlbuilder.vn.objects.Frame
import org.sqlbuilder.vn.objects.RestrainedRole

object Inherit {

    fun mergeRoles(restrainedRoles: Collection<RestrainedRole>, inheritedRestrainedRoles: Collection<RestrainedRole>): Collection<RestrainedRole> {
        // map child roles by type
        val map = restrainedRoles
            .asSequence()
            .map { it.roleType.type to it }
            .toMap()
            .toMutableMap()

        // map parent roles by type
        val inheritedMap = inheritedRestrainedRoles
            .asSequence()
            .map { it.roleType.type to it }
            .toMap()

        // merge roles : add role for which there is no overriding value in child
        inheritedMap
            .asSequence()
            .forEach {
                val inheritedType = it.key
                if (!map.containsKey(inheritedType)) {
                    map.put(inheritedType, it.value)
                }
            }
        return map.values
    }

    fun mergeFrames(frames: Collection<Frame>, inheritedFrames: Collection<Frame>): Collection<Frame> {
        return frames + inheritedFrames
    }
}
