package org.sqlbuilder.sl.foreign;

import org.sqlbuilder.common.Insertable;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class VnAlias implements Insertable
{
	public static final Comparator<VnAlias> COMPARATOR = Comparator.comparing(VnAlias::getPbRoleset).thenComparing(VnAlias::getVnClass);

	public static final Set<VnAlias> SET = new TreeSet<>(COMPARATOR);

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

	public String getVnClass()
	{
		return vnClass;
	}

	public String getPbRoleset()
	{
		return pbRoleset;
	}

	@Override
	public String dataRow()
	{
		return String.format("%s,%s", pbRoleset, vnClass);
	}
}
