package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.annotations.RequiresIdFrom;
import org.sqlbuilder.fn.objects.Governor;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.AnnoSetType;

public class Governor_AnnoSet extends Pair<Governor, Integer> implements Insertable
{
	public static final Comparator<Governor_AnnoSet> COMPARATOR = Comparator.comparing(Governor_AnnoSet::getFirst, Governor.COMPARATOR).thenComparing(Governor_AnnoSet::getSecond);

	public static final Set<Governor_AnnoSet> SET = new HashSet<>();

	// C O N S T R U C T O R

	@SuppressWarnings("UnusedReturnValue")
	public static Governor_AnnoSet make(final Governor governor, final AnnoSetType annoset)
	{
		var ga = new Governor_AnnoSet(governor, annoset.getID());
		SET.add(ga);
		return ga;
	}

	private Governor_AnnoSet(final Governor governor, final int annosetid)
	{
		super(governor, annosetid);
	}

	// I N S E R T

	@RequiresIdFrom(type = Governor.class)
	@Override
	public String dataRow()
	{
		return String.format("%s,%d", first.getSqlId(), second);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[GOV-AS governor=%s annosetid=%s]", first, second);
	}
}
