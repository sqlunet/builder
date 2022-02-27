package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.annotations.RequiresIdFrom;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.AnnoSetType;

public class FEGroupPattern_AnnoSet extends Pair<FEGroupPattern, Integer> implements Insertable
{
	public static final Set<FEGroupPattern_AnnoSet> SET = new HashSet<>();

	// C O N S T R U C T O R

	@SuppressWarnings("UnusedReturnValue")
	public static FEGroupPattern_AnnoSet make(final FEGroupPattern pattern, final AnnoSetType annoset)
	{
		var pa = new FEGroupPattern_AnnoSet(pattern, annoset.getID());
		SET.add(pa);
		return pa;
	}

	private FEGroupPattern_AnnoSet(final FEGroupPattern pattern, final int annosetid)
	{
		super(pattern, annosetid);
	}

	// I N S E R T A B L E

	@RequiresIdFrom(type = FEGroupPattern.class)
	@Override
	public String dataRow()
	{
		return String.format("%s,%d", first.getSqlId(), second);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[PAT-AS pattern=%s annosetid=%s]", first, second);
	}
}
