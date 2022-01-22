package org.sqlbuilder.fn.joins;

import org.sqlbuilder.fn.objects.Governor;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.AnnoSetType;

/*
governors_annosets.table=fngovernors_annosets
governors_annosets.create=CREATE TABLE IF NOT EXISTS %Fn_governors_annosets.table% ( governorid INTEGER NOT NULL,annosetid INTEGER NOT NULL,PRIMARY KEY (governorid,annosetid) );
governors_annosets.fk1=ALTER TABLE %Fn_governors_annosets.table% ADD CONSTRAINT fk_%Fn_governors_annosets.table%_governorid FOREIGN KEY (governorid) REFERENCES %Fn_governors.table% (governorid);
governors_annosets.fk2=ALTER TABLE %Fn_governors_annosets.table% ADD CONSTRAINT fk_%Fn_governors_annosets.table%_annosetid FOREIGN KEY (annosetid) REFERENCES %Fn_annosets.table% (annosetid);
governors_annosets.no-fk1=ALTER TABLE %Fn_governors_annosets.table% DROP CONSTRAINT fk_%Fn_governors_annosets.table%_annosetid CASCADE;
governors_annosets.no-fk2=ALTER TABLE %Fn_governors_annosets.table% DROP CONSTRAINT fk_%Fn_governors_annosets.table%_governorid CASCADE;
governors_annosets.insert=INSERT INTO %Fn_governors_annosets.table% (governorid,annosetid) VALUES(?,?);
 */
public class Governor_AnnoSet extends Pair<Governor, AnnoSetType>
{
	public static final Set<Governor_AnnoSet> SET = new HashSet<>();

	public Governor_AnnoSet(final Governor governor, final AnnoSetType annoset)
	{
		super(governor, annoset);
		SET.add(this);
	}

	@Override
	public String toString()
	{
		return String.format("[GOV-AS governor=%s annoset=%s]", this.first, this.second);
	}
}
