package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.objects.Governor;

import java.util.Comparator;
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
public class LexUnit_SemType extends Pair<Integer, Integer> implements Insertable<LexUnit_SemType>
{
	public static final Comparator<LexUnit_SemType> COMPARATOR = Comparator.comparing(LexUnit_SemType::getFirst).thenComparing(LexUnit_SemType::getSecond);

	public static final Set<LexUnit_SemType> SET = new HashSet<>();

	// C O N S T R U C T O R

	public static LexUnit_SemType make(final int luid, final SemTypeRefType semtype)
	{
		var ut = new LexUnit_SemType(luid, semtype.getID());
		SET.add(ut);
		return ut;
	}

	private LexUnit_SemType(final int luid, final int semtypeid)
	{
		super(luid, semtypeid);
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
		return String.format("[LU-SEM luid=%s semtypeid=%s]", first, second);
	}
}
