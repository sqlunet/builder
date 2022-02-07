package org.sqlbuilder.vn.objects;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Roles_Frames
{
	private final Map<String, Role> roles;

	private final List<Frame> frames;

	// C O N S T R U C T O R

	public static Roles_Frames make(final Collection<Role> roles, final List<Frame> frames)
	{
		var m = new Roles_Frames(roles, frames);
		return m;
	}

	private Roles_Frames(final Collection<Role> roles, final List<Frame> frames)
	{
		this.roles = new HashMap<>();
		for (final Role role : roles)
		{
			this.roles.put(role.getRoleType().getType(), role);
		}
		this.frames = frames;
	}

	// A C C E S S

	public Collection<Role> getRoles()
	{
		return this.roles.values();
	}

	public Collection<Frame> getFrames()
	{
		return this.frames;
	}

	// M E R G E

	public void merge(final Roles_Frames inheritedData)
	{
		// merge roles : add role for which there is no overriding value in child
		for (final Entry<String, Role> entry : inheritedData.roles.entrySet())
		{
			final String key = entry.getKey();
			if (!roles.containsKey(key))
			{
				roles.put(key, entry.getValue());
			}
		}
		frames.addAll(inheritedData.frames);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		for (final Role role : roles.values())
		{
			sb.append(" role=");
			sb.append(role.toString());
		}
		for (final Frame frame : frames)
		{
			sb.append("frame=");
			sb.append(frame.toString());
		}
		return sb.toString();
	}
}
