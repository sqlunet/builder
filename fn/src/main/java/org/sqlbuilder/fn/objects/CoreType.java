package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CoreType implements Insertable<CoreType>
{
	public static final Set<CoreType> SET = new HashSet<>();

	public static Map<CoreType, Integer> MAP;

	private final String coretype;

	public static final Comparator<CoreType> COMPARATOR = Comparator.comparing(t -> t.coretype);

	public CoreType(final String coretype)
	{
		this.coretype = coretype;
	}

	@Override
	public String dataRow()
	{
		return String.format("'%s'", Utils.escape(coretype));
	}
}
