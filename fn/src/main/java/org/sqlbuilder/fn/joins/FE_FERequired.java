package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.InternalFrameRelationFEType;

public class FE_FERequired extends Pair<Integer, Integer> implements Insertable
{
	public static final Set<FE_FERequired> SET = new HashSet<>();

	// C O N S T R U C T O R

	@SuppressWarnings("UnusedReturnValue")
	public static FE_FERequired make(final int fe, final InternalFrameRelationFEType fe2)
	{
		var ff = new FE_FERequired(fe, fe2.getID());
		SET.add(ff);
		return ff;
	}

	private FE_FERequired(final int feid, final int feid2)
	{
		super(feid, feid2);
	}

	// O R D E R

	public static final Comparator<FE_FERequired> COMPARATOR = Comparator.comparing(FE_FERequired::getFirst).thenComparing(FE_FERequired::getSecond);

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
		return String.format("[FE-FEreq feid=%s feid2=%s]", first, second);
	}
}
