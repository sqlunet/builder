package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.fn.objects.FE;
import org.sqlbuilder.fn.objects.FEGroupRealization;
import org.sqlbuilder.fn.types.FeType;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.FEValenceType;

public class FE_FEGroupRealization extends Pair<String, FEGroupRealization> implements Insertable
{
	public static final Set<FE_FEGroupRealization> SET = new HashSet<>();

	// C O N S T R U C T O R

	@SuppressWarnings("UnusedReturnValue")
	public static FE_FEGroupRealization make(final FEValenceType fe, final FEGroupRealization fegr)
	{
		var fr = new FE_FEGroupRealization(fe.getName(), fegr);
		SET.add(fr);
		return fr;
	}

	private FE_FEGroupRealization(final String feName, final FEGroupRealization fegr)
	{
		super(feName, fegr);
	}

	// A C C E S S

	String getFEName()
	{
		return first;
	}

	String getFENames()
	{
		return second.getFENames();
	}

	// O R D E R

	public static final Comparator<FE_FEGroupRealization> COMPARATOR = Comparator.comparing(FE_FEGroupRealization::getFEName).thenComparing(FE_FEGroupRealization::getFENames);

	// I N S E R T

	@RequiresIdFrom(type = FEGroupRealization.class)
	@Override
	public String dataRow()
	{
		// fegrid,feid,fetypeid
		int fetypeid = FeType.getIntId(first);
		var key = new Pair<>(fetypeid, second.getFrameID());
		var feid = FE.BY_FETYPEID_AND_FRAMEID.get(key).getID();

		return String.format("%s,%s,%s", //
				second.getSqlId(), //
				feid, //
				fetypeid);
	}

	@Override
	public String comment()
	{
		return String.format("fe=%s,fes={%s},luid=%d,frid=%d", first, getFENames(), second.getLuID(), second.getFrameID());
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[FE-FEGR fe=%s fegr={%s}]", first, second.getFENames());
	}
}
