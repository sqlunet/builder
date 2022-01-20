package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.objects.FEGroupRealization;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.FEGroupRealizationType.Pattern;

public class FnGroupPattern implements Insertable<FnGroupPattern>
{
	public static final Set<FnGroupPattern> SET = new HashSet<>();

	public final Pattern pattern;

	public final FEGroupRealization fegr;

	public FnGroupPattern(final FEGroupRealization fegr, final Pattern pattern)
	{
		this.fegr = fegr;
		this.pattern = pattern;
	}

	@Override
	public String dataRow()
	{
		// Long(1, getId());
		// Long(2, this.fegrid);
		// Long(3, this.pattern.getTotal());
		return null;
	}

	@Override
	public String toString()
	{
		return String.format("[GRPPAT pattern=%s fegrid=%s]", this.pattern, this.fegr);
	}
}
