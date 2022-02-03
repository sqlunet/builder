package org.sqlbuilder.vn.objects;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class VnClassData
{
	private final Map<String, VnRole> roles;

	private final List<VnFrame> frames;

	final VnClass vnclass;

	public VnClassData(final VnClass vnclass, final Collection<VnRole> roles, final List<VnFrame> frames)
	{
		this.roles = new HashMap<>();
		for (final VnRole role : roles)
		{
			this.roles.put(role.getRoleType().getType(), role);
		}
		this.frames = frames;
		this.vnclass = vnclass;
	}

	public void merge(final VnClassData inheritedData)
	{
		// merge roles : add role for which there is no overriding value in child
		for (final Entry<String, VnRole> entry : inheritedData.roles.entrySet())
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
		for (final VnRole role : this.roles.values())
		{
			buffer.append(" role=");
			buffer.append(role.toString());
		}
		for (final VnFrame frame : this.frames)
		{
			buffer.append("frame=");
			buffer.append(frame.toString());
		}
		return buffer.toString();
	}

	public Collection<VnRole> getRoles()
	{
		return this.roles.values();
	}

	public Collection<VnFrame> getFrames()
	{
		return this.frames;
	}
}
