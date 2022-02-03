package org.sqlbuilder.vn.objects;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ClassData
{
	private final Map<String, Role> roles;

	private final List<Frame> frames;

	final VnClass vnclass;

	public ClassData(final VnClass vnclass, final Collection<Role> roles, final List<Frame> frames)
	{
		this.roles = new HashMap<>();
		for (final Role role : roles)
		{
			this.roles.put(role.getRoleType().getType(), role);
		}
		this.frames = frames;
		this.vnclass = vnclass;
	}

	public void merge(final ClassData inheritedData)
	{
		// merge roles : add role for which there is no overriding value in child
		for (final Entry<String, Role> entry : inheritedData.roles.entrySet())
		{
			final String key = entry.getKey();
			if (!this.roles.containsKey(key))
			{
				this.roles.put(key, entry.getValue());
			}
		}
		this.frames.addAll(inheritedData.frames);
	}

	@Override
	public String toString()
	{
		final StringBuilder buffer = new StringBuilder();
		for (final Role role : this.roles.values())
		{
			buffer.append(" role=");
			buffer.append(role.toString());
		}
		for (final Frame frame : this.frames)
		{
			buffer.append("frame=");
			buffer.append(frame.toString());
		}
		return buffer.toString();
	}

	public Collection<Role> getRoles()
	{
		return this.roles.values();
	}

	public Collection<Frame> getFrames()
	{
		return this.frames;
	}
}
