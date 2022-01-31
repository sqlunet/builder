package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.RequiresIdFrom;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.AnnoSetType;

/*
grouppatterns_annosets.table=fnpatterns_annosets
grouppatterns_annosets.create=CREATE TABLE IF NOT EXISTS %Fn_patterns_annosets.table% ( patternid INTEGER NOT NULL,annosetid INTEGER NOT NULL,PRIMARY KEY (patternid,annosetid) );
grouppatterns_annosets.fk1=ALTER TABLE %Fn_patterns_annosets.table% ADD CONSTRAINT fk_%Fn_patterns_annosets.table%_patternid FOREIGN KEY (patternid) REFERENCES %Fn_patterns.table% (patternid);
grouppatterns_annosets.fk2=ALTER TABLE %Fn_patterns_annosets.table% ADD CONSTRAINT fk_%Fn_patterns_annosets.table%_annosetid FOREIGN KEY (annosetid) REFERENCES %Fn_annosets.table% (annosetid);
grouppatterns_annosets.no-fk1=ALTER TABLE %Fn_patterns_annosets.table% DROP CONSTRAINT fk_%Fn_patterns_annosets.table%_annosetid CASCADE;
grouppatterns_annosets.no-fk2=ALTER TABLE %Fn_patterns_annosets.table% DROP CONSTRAINT fk_%Fn_patterns_annosets.table%_patternid CASCADE;
grouppatterns_annosets.insert=INSERT INTO %Fn_patterns_annosets.table% (patternid,annosetid) VALUES(?,?);
 */
public class FEGroupPattern_AnnoSet extends Pair<FEGroupPattern, Integer> implements Insertable<FEGroupPattern_AnnoSet>
{
	public static final Set<FEGroupPattern_AnnoSet> SET = new HashSet<>();

	// C O N S T R U C T O R

	public static FEGroupPattern_AnnoSet make(final FEGroupPattern pattern, final AnnoSetType annoset)
	{
		var pa = new FEGroupPattern_AnnoSet(pattern, annoset.getID());
		SET.add(pa);
		return pa;
	}

	private FEGroupPattern_AnnoSet(final FEGroupPattern pattern, final int annosetid)
	{
		super(pattern, annosetid);
	}

	// I N S E R T A B L E

	@RequiresIdFrom(type = FEGroupPattern.class)
	@Override
	public String dataRow()
	{
		return String.format("%s,%d", first.getSqlId(), second);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[PAT-AS pattern=%s annosetid=%s]", first, second);
	}
}
