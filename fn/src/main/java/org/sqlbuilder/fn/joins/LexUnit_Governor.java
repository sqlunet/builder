package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.objects.Governor;

import java.util.HashSet;
import java.util.Set;

/*
lexunits_governors.table=fnlexunits_governors
lexunits_governors.create=CREATE TABLE IF NOT EXISTS %Fn_lexunits_governors.table% ( luid INTEGER NOT NULL,governorid INTEGER NOT NULL,PRIMARY KEY (luid,governorid) );
lexunits_governors.fk1=ALTER TABLE %Fn_lexunits_governors.table% ADD CONSTRAINT fk_%Fn_lexunits_governors.table%_luid FOREIGN KEY k_%Fn_lexunits_governors.table%_luid (luid) REFERENCES %Fn_lexunits.table% (luid);
lexunits_governors.fk2=ALTER TABLE %Fn_lexunits_governors.table% ADD CONSTRAINT fk_%Fn_lexunits_governors.table%_governorid FOREIGN KEY k_%Fn_lexunits_governors.table%_governorid (governorid) REFERENCES %Fn_governors.table% (governorid);
lexunits_governors.no-fk1=ALTER TABLE %Fn_lexunits_governors.table% DROP FOREIGN KEY fk_%Fn_lexunits_governors.table%_governorid;
lexunits_governors.no-fk2=ALTER TABLE %Fn_lexunits_governors.table% DROP FOREIGN KEY fk_%Fn_lexunits_governors.table%_luid;
lexunits_governors.insert=INSERT INTO %Fn_lexunits_governors.table% (luid,governorid) VALUES(?,?);
 */
public class LexUnit_Governor extends Pair<Integer, Governor> implements Insertable<LexUnit_Governor>
{
	public static final Set<LexUnit_Governor> SET = new HashSet<>();

	// C O N S T R U C T O R

	public static LexUnit_Governor make(final int luid, final Governor governor)
	{
		var ug = new LexUnit_Governor(luid, governor);
		SET.add(ug);
		return ug;
	}

	private LexUnit_Governor(final int luid, final Governor governor)
	{
		super(luid, governor);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("%d,%s", first, second.getId());
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[LU-GOV lu=%s governor=%s]", this.first, this.second);
	}
}
