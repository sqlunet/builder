package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class FE_SemType extends Pair<Integer, Integer> implements Insertable
{
	public static final Set<FE_SemType> SET = new HashSet<>();

	// C O N S T R U C T O R

	@SuppressWarnings("UnusedReturnValue")
	public static FE_SemType make(final int feid, final int semtypeid)
	{
		var fs = new FE_SemType(feid, semtypeid);
		SET.add(fs);
		return fs;
	}

	private FE_SemType(final int feid, final int semtypeid)
	{
		super(feid, semtypeid);
	}

	// O R D E R

	public static final Comparator<FE_SemType> COMPARATOR = Comparator.comparing(FE_SemType::getFirst).thenComparing(FE_SemType::getSecond);

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
		return String.format("[FE-SEM feid=%s semtypeid=%s]", first, second);
	}
}
