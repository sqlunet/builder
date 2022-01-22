package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.HasId;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.berkeley.icsi.framenet.FEGroupRealizationType;

/*
patterns.table=fnpatterns
patterns.create=CREATE TABLE IF NOT EXISTS %Fn_patterns.table% ( patternid INTEGER NOT NULL,fegrid INTEGER,total INTEGER,PRIMARY KEY (patternid) );
patterns.fk1=ALTER TABLE %Fn_patterns.table% ADD CONSTRAINT fk_%Fn_patterns.table%_fegrid FOREIGN KEY (fegrid) REFERENCES %Fn_fegrouprealizations.table% (fegrid);
patterns.no-fk1=ALTER TABLE %Fn_patterns.table% DROP CONSTRAINT fk_%Fn_patterns.table%_fegrid CASCADE;
patterns.insert=INSERT INTO %Fn_patterns.table% (patternid,fegrid,total) VALUES(?,?,?);
 */
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
