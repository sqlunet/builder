package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

/*
fes_semtypes.table=fnfes_semtypes
fes_semtypes.create=CREATE TABLE IF NOT EXISTS %Fn_fes_semtypes.table% ( feid INTEGER NOT NULL,semtypeid INTEGER NOT NULL,PRIMARY KEY (feid,semtypeid) );
fes_semtypes.fk1=ALTER TABLE %Fn_fes_semtypes.table% ADD CONSTRAINT fk_%Fn_fes_semtypes.table%_feid FOREIGN KEY (feid) REFERENCES %Fn_fes.table% (feid);
fes_semtypes.fk2=ALTER TABLE %Fn_fes_semtypes.table% ADD CONSTRAINT fk_%Fn_fes_semtypes.table%_semtypeid FOREIGN KEY (semtypeid) REFERENCES %Fn_semtypes.table% (semtypeid);
fes_semtypes.no-fk1=ALTER TABLE %Fn_fes_semtypes.table% DROP CONSTRAINT fk_%Fn_fes_semtypes.table%_feid CASCADE;
fes_semtypes.no-fk2=ALTER TABLE %Fn_fes_semtypes.table% DROP CONSTRAINT fk_%Fn_fes_semtypes.table%_semtypeid CASCADE;
fes_semtypes.insert=INSERT INTO %Fn_fes_semtypes.table% (feid,semtypeid) VALUES(?,?);
 */
public class FE_SemType extends Pair<Integer, Integer> implements Insertable<FE_SemType>
{
	public static final Set<FE_SemType> SET = new HashSet<>();

	// C O N S T R U C T O R

	public static FE_SemType make(final int feid, final int semtypeid)
	{
		var fs = new FE_SemType(feid, semtypeid);
		SET.add(fs);
		return fs;
	}

	private FE_SemType(final int feid, final int semtypeid)
	{
		super(feid, semtypeid);
	}

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
		return String.format("[FE-SEM feid=%s semtypeid=%s]", this.first, this.second);
	}
}
