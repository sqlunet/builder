package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.FEGroupRealizationType;

public class FnFEGroupRealization extends Pair<Long, FEGroupRealizationType> implements Insertable<FnFEGroupRealization>
{
	public static final Set<FnFEGroupRealization> SET = new HashSet<>();

	public FnFEGroupRealization(final long luid, final FEGroupRealizationType fer)
	{
		super(luid, fer);
	}

	@Override
	public String dataRow()
	{
		// fegrid INTEGER NOT NULL,
		// luid INTEGER,
		// total INTEGER,

		// Long(1, getId());
		// Long(2, this.lu);
		// Long(3, this.fer.getTotal());
		return null;
	}

	@Override
	public String toString()
	{
		return String.format("[FEGR fer=%s lu=%s]", second, first);
	}
}
