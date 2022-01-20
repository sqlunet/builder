package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.berkeley.icsi.framenet.SemTypeType;

public class FnSemType implements HasID, Insertable<FnSemType>
{
	public static final Set<FnSemType> SET = new HashSet<>();

	public static Map<FnSemType, Integer> MAP;

	public final SemTypeType type;

	public FnSemType(final SemTypeType type)
	{
		this.type = type;
	}

	public static final Comparator<FnSemType> COMPARATOR = Comparator.comparing(t -> t.type.getName());

	@Override
	public String dataRow()
	{
		return String.format("%d,'%s','%s','%s'", //
				type.getID(), //
				type.getName(), //
				type.getAbbrev(), //
				Utils.escape(type.getDefinition()));
	}

	@Override
	public String toString()
	{
		return String.format("[SEM semtypeid=%s name=%s]", this.type.getID(), this.type.getName());
	}
}
