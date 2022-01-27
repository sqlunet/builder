package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.objects.SemType;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.SemTypeType.SuperType;

/*
semtypes_supers.table=fnsemtypes_supers
semtypes_supers.create=CREATE TABLE IF NOT EXISTS %Fn_semtypes_supers.table% ( semtypeid INTEGER NOT NULL,supersemtypeid INTEGER NOT NULL,PRIMARY KEY (semtypeid,supersemtypeid) );
semtypes_supers.fk1=ALTER TABLE %Fn_semtypes_supers.table% ADD CONSTRAINT fk_%Fn_semtypes_supers.table%_semtypeid FOREIGN KEY (semtypeid) REFERENCES %Fn_semtypes.table% (semtypeid);
semtypes_supers.fk2=ALTER TABLE %Fn_semtypes_supers.table% ADD CONSTRAINT fk_%Fn_semtypes_supers.table%_supersemtypeid FOREIGN KEY (supersemtypeid) REFERENCES %Fn_semtypes.table% (semtypeid);
semtypes_supers.no-fk1=ALTER TABLE %Fn_semtypes_supers.table% DROP CONSTRAINT fk_%Fn_semtypes_supers.table%_semtypeid CASCADE;
semtypes_supers.no-fk2=ALTER TABLE %Fn_semtypes_supers.table% DROP CONSTRAINT fk_%Fn_semtypes_supers.table%_supersemtypeid CASCADE;
semtypes_supers.insert=INSERT INTO %Fn_semtypes_supers.table% (semtypeid,supersemtypeid) VALUES(?,?);
 */
public class SemType_SemTypeSuper extends Pair<Integer, Integer> implements Insertable<SemType_SemTypeSuper>
{
	public static final Set<SemType_SemTypeSuper> SET = new HashSet<>();

	// C O N S T R U C T O R

	public static SemType_SemTypeSuper make(final SemType semtype, final SuperType supersemtype)
	{
		var tt = new SemType_SemTypeSuper(semtype.getID(), supersemtype.getSupID());
		SET.add(tt);
		return tt;
	}

	private SemType_SemTypeSuper(final int semtypeid, final int supersemtypeid)
	{
		super(semtypeid, supersemtypeid);
	}

	// O R D E R

	public static final Comparator<SemType_SemTypeSuper> COMPARATOR = Comparator.comparing(SemType_SemTypeSuper::getFirst).thenComparing(SemType_SemTypeSuper::getSecond);

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("%d,%d", first, second);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[SEMsuper semtypeid=%s supersemtypeid=%s]", this.first, this.second);
	}
}
