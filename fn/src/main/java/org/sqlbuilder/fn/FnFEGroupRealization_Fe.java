package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.FEValenceType;

public class FnFEGroupRealization_Fe implements Insertable<FnFEGroupRealization_Fe>
{
	public static final Set<FnFEGroupRealization_Fe> SET = new HashSet<>();

	public final FEValenceType fe;

	public final FnFEGroupRealization fegr;

	public FnFEGroupRealization_Fe(final FnFEGroupRealization fegr, final FEValenceType fe)
	{
		this.fegr = fegr;
		this.fe = fe;
	}

	@Override
	public String dataRow()
	{
		// Long(1, this.rfeid);
		// Long(2, this.fegrid);
		// String(3, this.fe.getName());
		return null;
	}

	@Override
	public String toString()
	{
		return String.format("[FEGR-FE fegr=%s fe=%s]", this.fegr, this.fe.getName());
	}
}
