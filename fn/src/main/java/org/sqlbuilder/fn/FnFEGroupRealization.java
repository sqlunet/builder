package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.FEGroupRealizationType;

public class FnFEGroupRealization implements Insertable<FnFEGroupRealization>
{
	public static final Set<FnFEGroupRealization> SET = new HashSet<>();

	public final FEGroupRealizationType fer;

	public final FnLexUnit lu;

	public FnFEGroupRealization(final FnLexUnit lu, final FEGroupRealizationType fer)
	{
		this.lu = lu;
		this.fer = fer;
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
		return String.format("[FEGR fer=%s lu=%s]", fer, lu);
	}
}
