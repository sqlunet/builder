package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.fn.objects.FERealization;
import org.sqlbuilder.fn.objects.ValenceUnit;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class FEPattern extends Pair<FERealization, ValenceUnit> implements Insertable
{
	public static final Comparator<FEPattern> COMPARATOR = Comparator //
			.comparing(FEPattern::getLuId) //
			.thenComparing(FEPattern::getFeName) //
			.thenComparing(FEPattern::getSecond);

	public static final Set<FEPattern> SET = new HashSet<>();

	// C O N S T R U C T O R

	public static FEPattern make(final FERealization fer, final ValenceUnit vu)
	{
		var vr = new FEPattern(fer, vu);
		SET.add(vr);
		return vr;
	}

	private FEPattern(final FERealization fer, final ValenceUnit vu)
	{
		super(fer, vu);
	}

	// A C C E S S

	public FERealization getFer()
	{
		return first;
	}

	public String getFeName()
	{
		return first.getFEName();
	}

	public int getLuId()
	{
		return first.getLuId();
	}

	// I N S E R T

	@RequiresIdFrom(type = FERealization.class)
	@RequiresIdFrom(type = ValenceUnit.class)
	@Override
	public String dataRow()
	{
		return String.format("%s,%s", //
				first.getSqlId(), //
				second.getSqlId()); //
	}

	@Override
	public String comment()
	{
		return  String.format("luid=%d,fe=%s,vu={%s,%s,%s}", first.getLuId(), first.getFEName(), second.getFE(), second.getPT(), second.getGF());
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[FER-VU fer=%s vu=%s]", first, second);
	}
}
