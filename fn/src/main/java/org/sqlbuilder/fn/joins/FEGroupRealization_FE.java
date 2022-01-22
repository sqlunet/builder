package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.objects.FEGroupRealization;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.FEValenceType;

/*
fegrouprealizations_fes.table=fnfegrouprealizations_fes
fegrouprealizations_fes.create=CREATE TABLE IF NOT EXISTS %Fn_fegrouprealizations_fes.table% ( rfeid INTEGER,fegrid INTEGER NOT NULL,feid INTEGER DEFAULT NULL,fetypeid INTEGER DEFAULT NULL,PRIMARY KEY (rfeid) );
fegrouprealizations_fes.altcreate1=ALTER TABLE %Fn_fegrouprealizations_fes.table% ADD COLUMN fetype VARCHAR(30) AFTER fetypeid;
fegrouprealizations_fes.fk1=ALTER TABLE %Fn_fegrouprealizations_fes.table% ADD CONSTRAINT fk_%Fn_fegrouprealizations_fes.table%_fegrid FOREIGN KEY (fegrid) REFERENCES %Fn_fegrouprealizations.table% (fegrid);
fegrouprealizations_fes.fk2=ALTER TABLE %Fn_fegrouprealizations_fes.table% ADD CONSTRAINT fk_%Fn_fegrouprealizations_fes.table%_fetypeid FOREIGN KEY (fetypeid) REFERENCES %Fn_fetypes.table% (fetypeid);
fegrouprealizations_fes.fk3=ALTER TABLE %Fn_fegrouprealizations_fes.table% ADD CONSTRAINT fk_%Fn_fegrouprealizations_fes.table%_feid FOREIGN KEY (feid) REFERENCES %Fn_fes.table% (feid);
fegrouprealizations_fes.no-fk1=ALTER TABLE %Fn_fegrouprealizations_fes.table% DROP CONSTRAINT fk_%Fn_fegrouprealizations_fes.table%_fegrid CASCADE;
fegrouprealizations_fes.no-fk2=ALTER TABLE %Fn_fegrouprealizations_fes.table% DROP CONSTRAINT fk_%Fn_fegrouprealizations_fes.table%_fetypeid CASCADE;
fegrouprealizations_fes.no-fk3=ALTER TABLE %Fn_fegrouprealizations_fes.table% DROP CONSTRAINT fk_%Fn_fegrouprealizations_fes.table%_feid CASCADE;
fegrouprealizations_fes.insert=INSERT INTO %Fn_fegrouprealizations_fes.table% (rfeid,fegrid,fetype) VALUES(?,?,?);
 */
public class FEGroupRealization_FE extends Pair<FEGroupRealization, FEValenceType> implements Insertable<FEGroupRealization_FE>
{
	public static final Set<FEGroupRealization_FE> SET = new HashSet<>();

	public FEGroupRealization_FE(final FEGroupRealization fegr, final FEValenceType fe)
	{
		super(fegr, fe);
	}

	@Override
	public String dataRow()
	{
		// Long(1, this.rfeid);
		// Long(2, this.fegrid);
		// String(3, this.fe.getName());
		return String.format("",
				first,
				second.getName()
		);
	}

	@Override
	public String toString()
	{
		return String.format("[FEGR-FE fegr=%s fe=%s]", this.first, this.second.getName());
	}
}
