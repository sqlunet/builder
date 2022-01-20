package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Pos implements Insertable<Pos>
{
	public static final Set<Pos> SET = new HashSet<>();

	public static Map<Pos, Integer> MAP;

	public static final Comparator<Pos> COMPARATOR = Comparator.comparing(t -> t.pos);

	private final String pos;

	public Pos(final String pos)
	{
		this.pos = pos;
	}

	@Override
	public String dataRow()
	{
		return String.format("'%s'", pos);
	}
}
