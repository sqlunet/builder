package org.sqlbuilder.vn;

import org.sqlbuilder.vn.objects.Frame;
import org.sqlbuilder.vn.objects.Role;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Inherit
{
	public static Collection<Role> mergeRoles(final Collection<Role> roles, final Collection<Role> inheritedRoles)
	{
		// map child roles by type
		final Map<String, Role> map = roles.stream().collect(Collectors.toMap(r -> r.getRoleType().getType(), Function.identity()));

		// map parent roles by type
		final Map<String, Role> inheritedMap = inheritedRoles.stream().collect(Collectors.toMap(r -> r.getRoleType().getType(), Function.identity()));

		// merge roles : add role for which there is no overriding value in child
		for (final Entry<String, Role> inheritedEntry : inheritedMap.entrySet())
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
