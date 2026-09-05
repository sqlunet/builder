package org.semantikos.vn

import org.semantikos.vn.objects.Frame
import org.semantikos.vn.objects.RestrainedRole

object Inherit {

    fun mergeRoles(restrainedRoles: Collection<RestrainedRole>, inheritedRestrainedRoles: Collection<RestrainedRole>): Collection<RestrainedRole> {
        // map child roles by type
        val map = restrainedRoles
            .associateBy { it.roleType.type }
            .toMutableMap()

        // map parent roles by type
        val inheritedMap = inheritedRestrainedRoles
            .associateBy { it.roleType.type }

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
