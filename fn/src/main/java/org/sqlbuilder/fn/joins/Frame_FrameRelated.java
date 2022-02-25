package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.fn.types.FrameRelation;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.FrameIDNameType;

public class Frame_FrameRelated extends Pair<Integer, Integer> implements Insertable
{
	public static final Set<Frame_FrameRelated> SET = new HashSet<>();

	private final String relation;

	// C O N S T R U C T O R

	@SuppressWarnings("UnusedReturnValue")
	public static Frame_FrameRelated make(final int frameid, final FrameIDNameType frame2, final String relation)
	{
		var ff = new Frame_FrameRelated(frameid, frame2.getID(), relation);
		FrameRelation.add(relation);
		SET.add(ff);
		return ff;
	}

	private Frame_FrameRelated(final int frameid, final int frame2id, final String relation)
	{
		super(frameid, frame2id);
		this.relation = relation;
	}

	// A C C E S S

	public String getRelation()
	{
		return relation;
	}

	// O R D E R

	public static final Comparator<Frame_FrameRelated> COMPARATOR = Comparator.comparing(Frame_FrameRelated::getRelation).thenComparing(Pair::getFirst).thenComparing(Pair::getSecond);

	// I N S E R T

	@RequiresIdFrom(type = FrameRelation.class)
	@Override
	public String dataRow()
	{
		return String.format("%d,%d,%s", //
				first, //
				second, //
				FrameRelation.getSqlId(relation));
	}

	@Override
	public String comment()
	{
		return String.format("rel=%s", relation);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[relFR frameid=%s frame2id=%s type=%s]", first, second, relation);
	}
}
