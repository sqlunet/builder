package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.RequiresIdFrom;
import org.sqlbuilder.fn.objects.FERealization;
import org.sqlbuilder.fn.objects.Pattern;
import org.sqlbuilder.fn.objects.ValenceUnit;

import java.util.HashSet;
import java.util.Set;

/*
patterns_valenceunits.table=fnpatterns_valenceunits
patterns_valenceunits.create=CREATE TABLE IF NOT EXISTS %Fn_patterns_valenceunits.table% ( pvid INTEGER NOT NULL AUTO_INCREMENT,patternid INTEGER NOT NULL,vuid INTEGER NOT NULL,feid INTEGER,fetypeid INTEGER,PRIMARY KEY (pvid) );
patterns_valenceunits.altcreate1=ALTER TABLE %Fn_patterns_valenceunits.table% ADD COLUMN fetype VARCHAR(30) AFTER feid;
patterns_valenceunits.fk1=ALTER TABLE %Fn_patterns_valenceunits.table% ADD CONSTRAINT fk_%Fn_patterns_valenceunits.table%_patternid FOREIGN KEY (patternid) REFERENCES %Fn_patterns.table% (patternid);
patterns_valenceunits.fk2=ALTER TABLE %Fn_patterns_valenceunits.table% ADD CONSTRAINT fk_%Fn_patterns_valenceunits.table%_vuid FOREIGN KEY (vuid) REFERENCES %Fn_valenceunits.table% (vuid);
patterns_valenceunits.fk3=ALTER TABLE %Fn_patterns_valenceunits.table% ADD CONSTRAINT fk_%Fn_patterns_valenceunits.table%_feid FOREIGN KEY (feid) REFERENCES %Fn_fes.table% (feid);
patterns_valenceunits.fk4=ALTER TABLE %Fn_patterns_valenceunits.table% ADD CONSTRAINT fk_%Fn_patterns_valenceunits.table%_fetypeid FOREIGN KEY (fetypeid) REFERENCES %Fn_fetypes.table% (fetypeid);
patterns_valenceunits.no-fk1=ALTER TABLE %Fn_patterns_valenceunits.table% DROP CONSTRAINT fk_%Fn_patterns_valenceunits.table%_feid CASCADE;
patterns_valenceunits.no-fk2=ALTER TABLE %Fn_patterns_valenceunits.table% DROP CONSTRAINT fk_%Fn_patterns_valenceunits.table%_fetypeid CASCADE;
patterns_valenceunits.no-fk3=ALTER TABLE %Fn_patterns_valenceunits.table% DROP CONSTRAINT fk_%Fn_patterns_valenceunits.table%_patternid CASCADE;
patterns_valenceunits.no-fk4=ALTER TABLE %Fn_patterns_valenceunits.table% DROP CONSTRAINT fk_%Fn_patterns_valenceunits.table%_vuid CASCADE;
patterns_valenceunits.insert=INSERT INTO %Fn_patterns_valenceunits.table% (patternid,vuid,fetype) VALUES(?,?,?);
 */
public class Pattern_ValenceUnit extends Pair<Pattern, FERealization> implements Insertable<Pattern_ValenceUnit>
{
	public static final Set<Pattern_ValenceUnit> SET = new HashSet<>();

	public final ValenceUnit vu;

	// C O N S T R U C T O R

	public static Pattern_ValenceUnit make(final Pattern pattern, final FERealization fer, final ValenceUnit vu)
	{
		var pv = new Pattern_ValenceUnit(pattern, fer, vu);
		SET.add(pv);
		return pv;
	}

	private Pattern_ValenceUnit(final Pattern pattern, final FERealization fer, final ValenceUnit vu)
	{
		super(pattern, fer);
		this.vu = vu;
	}

	// I N S E R T

	@RequiresIdFrom(type = Pattern.class)
	@RequiresIdFrom(type = FERealization.class)
	@Override
	public String dataRow()
	{
		return String.format("%s,%s", first.getSqlId(), second.getSqlId());
	}

	@Override
	public String comment()
	{
		return String.format("vu={%s}", vu.comment());
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[PAT-VU pattern=%s fer=%s, vu=%s]", first, second, vu);
	}
}
