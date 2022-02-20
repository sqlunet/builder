package org.sqlbuilder.sl.foreign;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Resolvable;
import org.sqlbuilder2.ser.Pair;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

public class VnAlias implements Insertable, Resolvable<Pair<String, String>, Pair<Integer, Integer>>
{
	public static final Comparator<VnAlias> COMPARATOR = Comparator.comparing(VnAlias::getPbRoleset).thenComparing(VnAlias::getVnClass);

	public static final Set<VnAlias> SET = new TreeSet<>(COMPARATOR);

	public static Function<Pair<Integer, Integer>, String> RESOLVE_RESULT_STRINGIFIER = r -> //
			r == null ? "NULL,NULL" : String.format("%s,%s", r.first, r.second);

	private final String pbRoleset;

	private final String vnClass;

	// C O N S T R U C T O R

	public static VnAlias make(final String pbRoleSet, final String vnClass)
	{
		var a = new VnAlias(pbRoleSet, vnClass);
		SET.add(a);
		return a;
	}

	protected VnAlias(final String pbRoleSet, final String vnClass)
	{
		this.pbRoleset = pbRoleSet;
		this.vnClass = vnClass;
	}

	public String getPbRoleset()
	{
		return pbRoleset;
	}

	public String getVnClass()
	{
		return vnClass;
	}

	@Override
	public String dataRow()
	{
		return String.format("'%s','%s'", pbRoleset, vnClass);
	}

	// R E S O L V E

	@Override
	public Pair<String, String> resolving()
	{
		return new Pair<>(pbRoleset, vnClass);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("%s - %s", pbRoleset, vnClass);
	}
}
