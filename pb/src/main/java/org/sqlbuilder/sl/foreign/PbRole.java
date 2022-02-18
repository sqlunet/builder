package org.sqlbuilder.sl.foreign;

public class PbRole
{
	public final String roleSetName;

	public final String arg;

	public static PbRole make(final String roleSetName, final String arg)
	{
		return new PbRole(roleSetName, arg);
	}

	private PbRole(final String roleSetName, final String arg)
	{
		this.roleSetName = roleSetName;
		this.arg = arg;
	}
}
