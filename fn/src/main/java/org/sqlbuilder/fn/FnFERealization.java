package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.FERealizationType;

public class FnFERealization implements Insertable<FnFERealization>
{
	public static final Set<FnFERealization> SET = new HashSet<>();

	public final FERealizationType fer;

	public final FnLexUnit lu;

	public FnFERealization(final FnLexUnit lu, final FERealizationType fer)
	{
		this.lu = lu;
		this.fer = fer;
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
		return String.format("[FER lu=%s fe=%s]", this.lu, this.fer.getFE());
	}
}
