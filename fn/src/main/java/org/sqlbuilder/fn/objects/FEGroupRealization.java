package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.FEGroupRealizationType;

public class FEGroupRealization implements Insertable<FEGroupRealization>
{
	public static final Set<FEGroupRealization> SET = new HashSet<>();

	final FEGroupRealizationType fegr;

	final long luid;

	public FEGroupRealization(final FEGroupRealizationType fegr, final long luid)
	{
		this.fegr = fegr;
		this.luid = luid;
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
		return String.format("[FEGR fer=%s luid=%s]", fegr, luid);
	}
}
