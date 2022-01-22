package org.sqlbuilder.fn.joins;

import org.sqlbuilder.fn.objects.SemType;

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
public class SemType_SemTypeSuper extends Pair<SemType, SuperType>
{
	public static final Set<SemType_SemTypeSuper> SET = new HashSet<>();

	public SemType_SemTypeSuper(final SemType semtype, final SuperType supersemtype)
	{
		super(semtype, supersemtype);
	}

	@Override
	public String toString()
	{
		return String.format("[SEMsuper semtypeid=%s supersemtypeid=%s]", this.first, this.second);
	}
}
