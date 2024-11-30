package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

// TODO remove
public class Frame_CoreFEMember extends Pair<Integer, Integer> implements Insertable
{
	public static final Set<Frame_CoreFEMember> SET = new HashSet<>();

	// C O N S T R U C T O R

	public static Frame_CoreFEMember make(final int frameid, final int feid)
	{
		var fe = new Frame_CoreFEMember(frameid, feid);
		SET.add(fe);
		return fe;
	}

	private Frame_CoreFEMember(final int frameid, final int feid)
	{
		super(frameid, feid);
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
		return String.format("[FR-coreFE frameid=%s feid=%s]", first, second);
	}
}
