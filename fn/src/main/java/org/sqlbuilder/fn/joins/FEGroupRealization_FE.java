package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.objects.FEGroupRealization;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.FEValenceType;

public class FEGroupRealization_FE extends Pair<FEGroupRealization, FEValenceType> implements Insertable<FEGroupRealization_FE>
{
	public static final Set<FEGroupRealization_FE> SET = new HashSet<>();

	public FEGroupRealization_FE(final FEGroupRealization fegr, final FEValenceType fe)
	{
		super(fegr, fe);
	}

	@Override
	public String dataRow()
	{
		// Long(1, this.rfeid);
		// Long(2, this.fegrid);
		// String(3, this.fe.getName());
		return String.format("",
				first,
				second.getName()
		);
	}

	@Override
	public String toString()
	{
		return String.format("[FEGR-FE fegr=%s fe=%s]", this.first, this.second.getName());
	}
}
