package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.SemTypeRefType;

public class LexUnit_SemType extends Pair<Integer, Integer> implements Insertable
{
	public static final Comparator<LexUnit_SemType> COMPARATOR = Comparator.comparing(LexUnit_SemType::getFirst).thenComparing(LexUnit_SemType::getSecond);

	public static final Set<LexUnit_SemType> SET = new HashSet<>();

	// C O N S T R U C T O R

	@SuppressWarnings("UnusedReturnValue")
	public static LexUnit_SemType make(final int luid, final SemTypeRefType semtype)
	{
		var ut = new LexUnit_SemType(luid, semtype.getID());
		SET.add(ut);
		return ut;
	}

	private LexUnit_SemType(final int luid, final int semtypeid)
	{
		super(luid, semtypeid);
	}

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
		return String.format("[LU-SEM luid=%s semtypeid=%s]", first, second);
	}
}
