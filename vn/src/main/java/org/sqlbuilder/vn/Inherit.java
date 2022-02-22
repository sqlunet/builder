package org.sqlbuilder.vn;

import org.sqlbuilder.vn.objects.Frame;
import org.sqlbuilder.vn.objects.RestrainedRole;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Inherit
{
	public static Collection<RestrainedRole> mergeRoles(final Collection<RestrainedRole> restrainedRoles, final Collection<RestrainedRole> inheritedRestrainedRoles)
	{
		// map child roles by type
		final Map<String, RestrainedRole> map = restrainedRoles.stream().collect(Collectors.toMap(r -> r.getRoleType().getType(), Function.identity()));

		// map parent roles by type
		final Map<String, RestrainedRole> inheritedMap = inheritedRestrainedRoles.stream().collect(Collectors.toMap(r -> r.getRoleType().getType(), Function.identity()));

		// merge roles : add role for which there is no overriding value in child
		for (final Entry<String, RestrainedRole> inheritedEntry : inheritedMap.entrySet())
		{
			final String inheritedType = inheritedEntry.getKey();
			if (!map.containsKey(inheritedType))
			{
				map.put(inheritedType, inheritedEntry.getValue());
			}
		}
		return map.values();
	}

	public static Collection<Frame> mergeFrames(final Collection<Frame> frames, final Collection<Frame> inheritedFrames)
	{
		frames.addAll(inheritedFrames);
		return frames;
	}
}
