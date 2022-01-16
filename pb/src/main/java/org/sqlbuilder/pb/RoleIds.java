package org.sqlbuilder.pb;

public class RoleIds
{
	public final long setId;

	public final long id;

	public RoleIds(final long setId, final long id)
	{
		this.setId = setId;
		this.id = id;
	}

	@Override
	public String toString()
	{
		return String.format("%s-%s", this.setId, this.id);
	}
}