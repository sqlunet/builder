package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.objects.FERealization;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.FEValenceType;

/*
/*
ferealizations.table=fnferealizations
ferealizations.create=CREATE TABLE IF NOT EXISTS %Fn_ferealizations.table% ( ferid INTEGER NOT NULL,luid INTEGER,fetypeid INTEGER DEFAULT NULL,feid INTEGER DEFAULT NULL,total INTEGER,PRIMARY KEY (ferid) );
ferealizations.altcreate1=ALTER TABLE %Fn_ferealizations.table% ADD COLUMN fetype VARCHAR(30) AFTER fetypeid;
ferealizations.fk1=ALTER TABLE %Fn_ferealizations.table% ADD CONSTRAINT fk_%Fn_ferealizations.table%_luid FOREIGN KEY (luid) REFERENCES %Fn_lexunits.table% (luid);
ferealizations.fk2=ALTER TABLE %Fn_ferealizations.table% ADD CONSTRAINT fk_%Fn_ferealizations.table%_fetypeid FOREIGN KEY (fetypeid) REFERENCES %Fn_fetypes.table% (fetypeid);
ferealizations.no-fk1=ALTER TABLE %Fn_ferealizations.table% DROP CONSTRAINT fk_%Fn_ferealizations.table%_fetypeid CASCADE;
ferealizations.no-fk2=ALTER TABLE %Fn_ferealizations.table% DROP CONSTRAINT fk_%Fn_ferealizations.table%_luid CASCADE;
ferealizations.insert=INSERT INTO %Fn_ferealizations.table% (ferid,luid,fetype,total) VALUES(?,?,?,?);
 */
public class FE_FERealization extends Pair<FEValenceType, FERealization> implements Insertable<FE_FERealization>
{
	public static final Set<FE_FERealization> SET = new HashSet<>();

	public FE_FERealization(final FEValenceType fe, final FERealization fer)
	{
		super(fe, fer);
		SET.add(this);
	}

	@Override
	public String dataRow()
	{
		return String.format("%s,%s", //
				first.getName(), //
				second.dataRow());
	}

	@Override
	public String toString()
	{
		return String.format("[FE-FER fe=%s fer=%s]", this.first, this.second);
	}
}
