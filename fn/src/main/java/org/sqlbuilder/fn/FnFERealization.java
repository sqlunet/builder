package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.FERealizationType;

public class FnFERealization extends Pair<Long, FERealizationType> implements Insertable<FnFERealization>
{
	public static final Set<FnFERealization> SET = new HashSet<>();

	public FnFERealization(final long luid, final FERealizationType fer)
	{
		super(luid, fer);
	}

	@Override
	public String dataRow()
	{
		// Long(1, getId());
		// Long(2, this.luid);
		// String(3, this.fer.getFE().getName());
		// Long(4, this.fer.getTotal());
		return null;
	}

	@Override
	public String toString()
	{
		return String.format("[FER luid=%s fe=%s]", this.first, this.second.getFE());
	}
}
