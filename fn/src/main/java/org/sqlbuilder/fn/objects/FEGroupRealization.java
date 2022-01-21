package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.HasId;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import edu.berkeley.icsi.framenet.FEGroupRealizationType;
import edu.berkeley.icsi.framenet.FEValenceType;

public class FEGroupRealization implements HasId, Insertable<FEGroupRealization>
{
	public static final Set<FEGroupRealization> SET = new HashSet<>();

	public static Map<FEGroupRealization,Integer> MAP;

	final FEGroupRealizationType fegr;

	final int luid;

	public FEGroupRealization(final FEGroupRealizationType fegr, final int luid)
	{
		this.fegr = fegr;
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
				getId(), //
				Arrays.stream(fegr.getFEArray()).map(FEValenceType::getName).collect(Collectors.joining(",")), //
				fegr.getTotal(), //
				luid);
	}

	@Override
	public String toString()
	{
		return String.format("[FEGR fegr=%s luid=%s]", fegr, luid);
	}
}
