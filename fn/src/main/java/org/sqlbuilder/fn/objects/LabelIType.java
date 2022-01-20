package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LabelIType implements Insertable<LabelIType>
{
	public static final Set<LabelIType> SET = new HashSet<>();

	public static Map<LabelIType, Integer> MAP;

	public static final Comparator<LabelIType> COMPARATOR = Comparator.comparing(t -> t.labelitaype);

	private final String labelitaype;

	public LabelIType(final String labelitype)
	{
		this.labelitaype = labelitype;
	}

	@Override
	public String dataRow()
	{
		return String.format("'%s'", labelitaype);
	}
}
