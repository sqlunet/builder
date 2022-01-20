package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FnLabelIType extends FnName implements Insertable<FnLabelIType>
{
	public static final Set<FnLabelIType> SET = new HashSet<>();

	public static Map<FnLabelIType,Integer> MAP;

	public static final Comparator<FnLabelIType> COMPARATOR = Comparator.comparing(t -> t.name);
	public FnLabelIType(final String labelitype)
	{
		super(labelitype);
	}

	@Override
	public String dataRow()
	{
		return String.format("'%s'", Utils.escape(name));
	}
}
