package org.sqlbuilder.fn.joins;

import org.sqlbuilder.fn.objects.Pattern;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.AnnoSetType;

/*
patterns_annosets.table=fnpatterns_annosets
patterns_annosets.create=CREATE TABLE IF NOT EXISTS %Fn_patterns_annosets.table% ( patternid INTEGER NOT NULL,annosetid INTEGER NOT NULL,PRIMARY KEY (patternid,annosetid) );
patterns_annosets.fk1=ALTER TABLE %Fn_patterns_annosets.table% ADD CONSTRAINT fk_%Fn_patterns_annosets.table%_patternid FOREIGN KEY (patternid) REFERENCES %Fn_patterns.table% (patternid);
patterns_annosets.fk2=ALTER TABLE %Fn_patterns_annosets.table% ADD CONSTRAINT fk_%Fn_patterns_annosets.table%_annosetid FOREIGN KEY (annosetid) REFERENCES %Fn_annosets.table% (annosetid);
patterns_annosets.no-fk1=ALTER TABLE %Fn_patterns_annosets.table% DROP CONSTRAINT fk_%Fn_patterns_annosets.table%_annosetid CASCADE;
patterns_annosets.no-fk2=ALTER TABLE %Fn_patterns_annosets.table% DROP CONSTRAINT fk_%Fn_patterns_annosets.table%_patternid CASCADE;
patterns_annosets.insert=INSERT INTO %Fn_patterns_annosets.table% (patternid,annosetid) VALUES(?,?);
 */
public class Pattern_AnnoSet extends Pair<Pattern, AnnoSetType>
{
	public static final Set<Pattern_AnnoSet> SET = new HashSet<>();

	public Pattern_AnnoSet(final Pattern pattern, final AnnoSetType annoset)
	{
		super(pattern, annoset);
	}

	@Override
	public String toString()
	{
		return String.format("[PAT-AS pattern=%s annoset=%s]", this.first, this.second);
	}
}
