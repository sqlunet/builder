package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Frame_SemType extends Pair<Integer, Integer> implements Insertable<Frame_SemType>
{
	public static final Set<Frame_SemType> SET = new HashSet<>();

	// C O N S T R U C T O R

	public static Frame_SemType make(final int frameid, final int semtypeid)
	{
		var ft = new Frame_SemType(frameid, semtypeid);
		SET.add(ft);
		return ft;
	}

	private Frame_SemType(final int frameid, final int semtypeid)
	{
		super(frameid, semtypeid);
	}

	// O R D E R

	public static final Comparator<Frame_SemType> COMPARATOR = Comparator.comparing(Frame_SemType::getFirst).thenComparing(Frame_SemType::getSecond);

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
		return String.format("[FR-SEM frameid=%s semtypeid=%s]", first, second);
	}
}
