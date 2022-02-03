package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.RequiresIdFrom;
import org.sqlbuilder.fn.objects.Governor;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class LexUnit_Governor extends Pair<Integer, Governor> implements Insertable
{
	public static final Comparator<LexUnit_Governor> COMPARATOR = Comparator.comparing(LexUnit_Governor::getFirst).thenComparing(LexUnit_Governor::getSecond, Governor.COMPARATOR);

	public static final Set<LexUnit_Governor> SET = new HashSet<>();

	// C O N S T R U C T O R

	public static LexUnit_Governor make(final int luid, final Governor governor)
	{
		var ug = new LexUnit_Governor(luid, governor);
		SET.add(ug);
		return ug;
	}

	private LexUnit_Governor(final int luid, final Governor governor)
	{
		super(luid, governor);
	}

	// I N S E R T

	@RequiresIdFrom(type = Governor.class)
	@Override
	public String dataRow()
	{
		return String.format("%d,%s", first, second.getSqlId());
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[LU-GOV lu=%s governor=%s]", first, second);
	}
}
