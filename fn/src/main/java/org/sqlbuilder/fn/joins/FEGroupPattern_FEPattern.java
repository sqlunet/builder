package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.RequiresIdFrom;
import org.sqlbuilder.fn.objects.FERealization;
import org.sqlbuilder.fn.objects.ValenceUnit;

import java.util.HashSet;
import java.util.Set;

/*
grouppatterns_patterns.table=fngrouppatterns_patterns
grouppatterns_patterns.create=CREATE TABLE IF NOT EXISTS %Fn_grouppatterns_patterns.table% ( pvid INTEGER NOT NULL AUTO_INCREMENT,patternid INTEGER NOT NULL,vuid INTEGER NOT NULL,feid INTEGER,fetypeid INTEGER,PRIMARY KEY (pvid) );
grouppatterns_patterns.altcreate1=ALTER TABLE %Fn_grouppatterns_patterns.table% ADD COLUMN fetype VARCHAR(30) AFTER feid;
grouppatterns_patterns.fk1=ALTER TABLE %Fn_grouppatterns_patterns.table% ADD CONSTRAINT fk_%Fn_grouppatterns_patterns.table%_patternid FOREIGN KEY (patternid) REFERENCES %Fn_patterns.table% (patternid);
grouppatterns_patterns.fk2=ALTER TABLE %Fn_grouppatterns_patterns.table% ADD CONSTRAINT fk_%Fn_grouppatterns_patterns.table%_vuid FOREIGN KEY (vuid) REFERENCES %Fn_valenceunits.table% (vuid);
grouppatterns_patterns.fk3=ALTER TABLE %Fn_grouppatterns_patterns.table% ADD CONSTRAINT fk_%Fn_grouppatterns_patterns.table%_feid FOREIGN KEY (feid) REFERENCES %Fn_fes.table% (feid);
grouppatterns_patterns.fk4=ALTER TABLE %Fn_grouppatterns_patterns.table% ADD CONSTRAINT fk_%Fn_grouppatterns_patterns.table%_fetypeid FOREIGN KEY (fetypeid) REFERENCES %Fn_fetypes.table% (fetypeid);
grouppatterns_patterns.no-fk1=ALTER TABLE %Fn_grouppatterns_patterns.table% DROP CONSTRAINT fk_%Fn_grouppatterns_patterns.table%_feid CASCADE;
grouppatterns_patterns.no-fk2=ALTER TABLE %Fn_grouppatterns_patterns.table% DROP CONSTRAINT fk_%Fn_grouppatterns_patterns.table%_fetypeid CASCADE;
grouppatterns_patterns.no-fk3=ALTER TABLE %Fn_grouppatterns_patterns.table% DROP CONSTRAINT fk_%Fn_grouppatterns_patterns.table%_patternid CASCADE;
grouppatterns_patterns.no-fk4=ALTER TABLE %Fn_grouppatterns_patterns.table% DROP CONSTRAINT fk_%Fn_grouppatterns_patterns.table%_vuid CASCADE;
grouppatterns_patterns.insert=INSERT INTO %Fn_grouppatterns_patterns.table% (patternid,vuid,fetype) VALUES(?,?,?);
 */
public class FEGroupPattern_FEPattern extends Triple<FEGroupPattern, FERealization, ValenceUnit> implements Insertable<FEGroupPattern_FEPattern>
{
	public static final Set<FEGroupPattern_FEPattern> SET = new HashSet<>();

	// C O N S T R U C T O R

	public static FEGroupPattern_FEPattern make(final FEGroupPattern groupPattern, final FERealization fer, final ValenceUnit vu)
	{
		var p = new FEGroupPattern_FEPattern(groupPattern, fer, vu);
		SET.add(p);
		return p;
	}

	private FEGroupPattern_FEPattern(final FEGroupPattern groupPattern, final FERealization fer, final ValenceUnit vu)
	{
		super(groupPattern, fer, vu);
	}

	// I N S E R T

	@RequiresIdFrom(type = FEGroupPattern.class)
	@RequiresIdFrom(type = FERealization.class)
	@RequiresIdFrom(type = ValenceUnit.class)
	@Override
	// grouppatternid, ferid, vuid
	public String dataRow()
	{
		return String.format("%s,%s,%s", first.getSqlId(), second.getSqlId(), third.getSqlId());
	}

	@Override
	public String comment()
	{
		return String.format("fegr={%s} fer={%s} vu={%s}", first.comment(), second.comment(), third.comment());
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[PAT-VU fer=%s pattern=%s, vu=%s]", second, first, first);
	}
}
