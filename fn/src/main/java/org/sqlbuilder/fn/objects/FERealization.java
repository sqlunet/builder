package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.HasId;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.berkeley.icsi.framenet.FERealizationType;

public class FERealization implements HasId, Insertable<FERealization>
{
	public static final Set<FERealization> SET = new HashSet<>();

	public static Map<FERealization, Integer> MAP;

	private final FERealizationType fer;

	private final int luid;

	public FERealization(final FERealizationType fer, final int luid)
	{
		this.fer = fer;
		this.luid = luid;
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
		return String.format("%s,'%s',%s,%d", //
				getId(), // fer.getId()
				fer.getFE().getName(), fer.getTotal(), //
				luid);
	}

	@Override
	public String toString()
	{
		return String.format("[FER fe=%s luid=%s]", fer.getFE(), luid);
	}
}
