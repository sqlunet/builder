package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.FEGroupRealizationType;

public class FEGroupRealization implements Insertable<FEGroupRealization>
{
	public static final Set<FEGroupRealization> SET = new HashSet<>();

	final FEGroupRealizationType fegr;

	final int luid;

	public FEGroupRealization(final FEGroupRealizationType fegr, final int luid)
	{
		this.fegr = fegr;
		this.luid = luid;
	}

	@Override
	public String dataRow()
	{
		return String.format("%s,%s,%d", //
				"NULL", // fegr.getId()
				fegr.getTotal(), //
				luid);
	}

	@Override
	public String toString()
	{
		return String.format("[FEGR fegr=%s luid=%s]", fegr, luid);
	}
}
