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

/*
fegrouprealizations.table=fnfegrouprealizations
fegrouprealizations.create=CREATE TABLE IF NOT EXISTS %Fn_fegrouprealizations.table% ( fegrid INTEGER NOT NULL,luid INTEGER,total INTEGER,PRIMARY KEY (fegrid) );
fegrouprealizations.fk1=ALTER TABLE %Fn_fegrouprealizations.table% ADD CONSTRAINT fk_%Fn_fegrouprealizations.table%_luid FOREIGN KEY (luid) REFERENCES %Fn_lexunits.table% (luid);
fegrouprealizations.no-fk1=ALTER TABLE %Fn_fegrouprealizations.table% DROP CONSTRAINT fk_%Fn_fegrouprealizations.table%_luid CASCADE;
fegrouprealizations.insert=INSERT INTO %Fn_fegrouprealizations.table% (fegrid,luid,total) VALUES(?,?,?);
 */
public class FEGroupRealization implements HasId, Insertable<FEGroupRealization>
{
	public static final Set<FEGroupRealization> SET = new HashSet<>();

	public static Map<FEGroupRealization, Integer> MAP;

	final FEGroupRealizationType fegr;

	final int luid;

	public FEGroupRealization(final FEGroupRealizationType fegr, final int luid)
	{
		this.fegr = fegr;
		this.luid = luid;
		SET.add(this);
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
		// fegrid INTEGER NOT NULL,
		// total INTEGER,
		// luid INTEGER,
		return String.format("%s,'%s',%s,%d", //
				getId(), //
				fegr.getTotal(), //
				luid);
	}

	@Override
	public String comment()
	{
		return String.format("%s", //
				Arrays.stream(fegr.getFEArray()).map(FEValenceType::getName).collect(Collectors.joining(",")));
	}

	@Override
	public String toString()
	{
		return String.format("[FEGR fegr=%s luid=%s]", fegr, luid);
	}
}
