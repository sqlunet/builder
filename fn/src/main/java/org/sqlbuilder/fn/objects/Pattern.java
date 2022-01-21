package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.HasId;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.berkeley.icsi.framenet.FEGroupRealizationType;

public class Pattern implements HasId, Insertable<Pattern>
{
	public static final Set<Pattern> SET = new HashSet<>();

	public static Map<Pattern,Integer> MAP;

	public final FEGroupRealizationType.Pattern pattern;

	public final FEGroupRealization fegr;

	public Pattern(final FEGroupRealizationType.Pattern pattern, final FEGroupRealization fegr)
	{
		this.fegr = fegr;
		this.pattern = pattern;
	}

	@Override
	public Object getId()
	{
		Integer id = MAP.get(this);
		if (id != null)
		{
			return id;
		}
		return "NULL";
	}

	@Override
	public String dataRow()
	{
		return String.format("%s,%d,%s",
				getId(), //
				pattern.getTotal(), //
				fegr.getId()
		);
	}

	@Override
	public String toString()
	{
		return String.format("[GRPPAT pattern=%s fegr=%s]", this.pattern, this.fegr);
	}
}
