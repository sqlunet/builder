package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FnPos extends FnName implements Insertable<FnPos>
{
	public static final Set<FnPos> SET = new HashSet<>();

	public static Map<FnPos,Integer> MAP;

	public static final Comparator<FnPos> COMPARATOR = Comparator.comparing(t -> t.name);

	public FnPos(final String pos)
	{
		super(pos);
	}

	@Override
	public String dataRow()
	{
		return String.format("'%s'", Utils.escape(name));
	}
}
