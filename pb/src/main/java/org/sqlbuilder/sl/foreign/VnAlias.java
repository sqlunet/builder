package org.sqlbuilder.sl.foreign;

public class VnAlias
{
	private final String vnClass;

	private final String pbRoleset;

	// C O N S T R U C T O R

	public static VnAlias make(final String vnClass, final String pbRoleSet)
	{
		var a = new VnAlias(vnClass, pbRoleSet);
		return a;
	}

	protected VnAlias(final String vnClass, final String pbRoleSet)
	{
		this.vnClass = vnClass;
		this.pbRoleset = pbRoleSet;
	}
}
