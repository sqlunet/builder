package org.sqlbuilder.fn.joins;


import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.SemTypeRefType;

/*
lexunits_semtypes.table=fnlexunits_semtypes
lexunits_semtypes.create=CREATE TABLE IF NOT EXISTS %Fn_lexunits_semtypes.table% ( luid INTEGER NOT NULL,semtypeid INTEGER NOT NULL,PRIMARY KEY (luid,semtypeid) );
lexunits_semtypes.fk1=ALTER TABLE %Fn_lexunits_semtypes.table% ADD CONSTRAINT fk_%Fn_lexunits_semtypes.table%_luid FOREIGN KEY (luid) REFERENCES %Fn_lexunits.table% (luid);
lexunits_semtypes.fk2=ALTER TABLE %Fn_lexunits_semtypes.table% ADD CONSTRAINT fk_%Fn_lexunits_semtypes.table%_semtypeid FOREIGN KEY (semtypeid) REFERENCES %Fn_semtypes.table% (semtypeid);
lexunits_semtypes.no-fk1=ALTER TABLE %Fn_lexunits_semtypes.table% DROP CONSTRAINT fk_%Fn_lexunits_semtypes.table%_luid CASCADE;
lexunits_semtypes.no-fk2=ALTER TABLE %Fn_lexunits_semtypes.table% DROP CONSTRAINT fk_%Fn_lexunits_semtypes.table%_semtypeid CASCADE;
lexunits_semtypes.insert=INSERT INTO %Fn_lexunits_semtypes.table% (luid,semtypeid) VALUES(?,?);
 */
public class LexUnit_SemType extends Pair<Long, SemTypeRefType>
{
	public static final Set<LexUnit_SemType> SET = new HashSet<>();

	public LexUnit_SemType(final long luid, final SemTypeRefType semtype)
	{
		super(luid, semtype);
	}

	@Override
	public String toString()
	{
		return String.format("[LU-SEM luid=%s semtype=%s]", this.first, this.second);
	}
}
