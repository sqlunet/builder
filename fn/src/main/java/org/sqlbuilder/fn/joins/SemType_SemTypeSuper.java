package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.objects.SemType;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.SemTypeType.SuperType;

public class SemType_SemTypeSuper extends Pair<Integer, Integer> implements Insertable
{
	public static final Set<SemType_SemTypeSuper> SET = new HashSet<>();

	// C O N S T R U C T O R

	@SuppressWarnings("UnusedReturnValue")
	public static SemType_SemTypeSuper make(final SemType semtype, final SuperType supersemtype)
	{
		var tt = new SemType_SemTypeSuper(semtype.getID(), supersemtype.getSupID());
		SET.add(tt);
		return tt;
	}

	private SemType_SemTypeSuper(final int semtypeid, final int supersemtypeid)
	{
		super(semtypeid, supersemtypeid);
	}

	// O R D E R

	public static final Comparator<SemType_SemTypeSuper> COMPARATOR = Comparator.comparing(SemType_SemTypeSuper::getFirst).thenComparing(SemType_SemTypeSuper::getSecond);

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("%d,%d", first, second);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[superSEM semtypeid=%s supersemtypeid=%s]", first, second);
	}
}
