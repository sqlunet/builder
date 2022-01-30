package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.RequiresIdFrom;
import org.sqlbuilder.fn.objects.Governor;

import java.util.Comparator;
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
public class Governor_AnnoSet extends Pair<Governor, Integer> implements Insertable<Governor_AnnoSet>
{
	public static final Comparator<Governor_AnnoSet> COMPARATOR = Comparator.comparing(Governor_AnnoSet::getFirst, Governor.COMPARATOR).thenComparing(Governor_AnnoSet::getSecond);

	public static final Set<Governor_AnnoSet> SET = new HashSet<>();

	// C O N S T R U C T O R

	public static Governor_AnnoSet make(final Governor governor, final AnnoSetType annoset)
	{
		var ga = new Governor_AnnoSet(governor, annoset.getID());
		SET.add(ga);
		return ga;
	}

	private Governor_AnnoSet(final Governor governor, final int annosetid)
	{
		super(governor, annosetid);
	}

	// I N S E R T

	@RequiresIdFrom(type = Governor.class)
	@Override
	public String dataRow()
	{
		return String.format("%s,%d", first.getSqlId(), second);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[GOV-AS governor=%s annosetid=%s]", first, second);
	}
}
