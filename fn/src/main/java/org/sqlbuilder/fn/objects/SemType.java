package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.HasID;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.berkeley.icsi.framenet.SemTypeType;

public class SemType implements HasID, Insertable<SemType>
{
	public static final Set<SemType> SET = new HashSet<>();

	public final SemTypeType type;

	public SemType(final SemTypeType type)
	{
		this.type = type;
	}

	public static final Comparator<SemType> COMPARATOR = Comparator.comparing(t -> t.type.getName());

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
