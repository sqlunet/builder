package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FnCoreType extends FnName implements Insertable<FnCoreType>
{
	public static final Set<FnCoreType> SET = new HashSet<>();

	public static Map<FnCoreType,Integer> MAP;

	public static final Comparator<FnCoreType> COMPARATOR = Comparator.comparing(t -> t.name);

	public FnCoreType(final String coretype)
	{
		super(coretype);
	}

	@Override
	public String dataRow()
	{
		return String.format("'%s'", Utils.escape(name));
	}
}
