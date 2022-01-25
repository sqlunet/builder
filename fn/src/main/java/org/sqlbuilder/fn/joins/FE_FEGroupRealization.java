package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.objects.FE;
import org.sqlbuilder.fn.objects.FEGroupRealization;
import org.sqlbuilder.fn.types.FeType;

import java.util.Comparator;
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
public class FE_FEGroupRealization extends Pair<FEValenceType, FEGroupRealization> implements Insertable<FE_FEGroupRealization>
{
	public static final Set<FE_FEGroupRealization> SET = new HashSet<>();

	public FE_FEGroupRealization(final FEValenceType fe, final FEGroupRealization fegr)
	{
		super(fe, fegr);
		SET.add(this);
	}

	// A C C E S S

	String getFEName()
	{
		return first.getName();
	}

	String getFENames()
	{
		return second.getFENames();
	}

	// I D E N T I T Y
	// pair

	// O R D E R

	public static final Comparator<FE_FEGroupRealization> COMPARATOR = Comparator.comparing(FE_FEGroupRealization::getFEName).thenComparing(FE_FEGroupRealization::getFENames);

	// I N S E R T

	@Override
	public String dataRow()
	{
		// (rfeid),fegrid,feid,fetypeid
		String feName = first.getName();
		int fetypeid = FeType.getIntId(feName);
		var key = new Pair<>(fetypeid, second.getFrameID());
		var feid = FE.BY_FETYPEID_AND_FRAMEID.get(key).getID();

		return String.format("%s,%s,%s", //
				second.getId(), //
				feid, //
				fetypeid);
	}

	@Override
	public String comment()
	{
		return String.format("%s,{%s},%d,%d", getFEName(), getFENames(),second.getLuID(), second.getFrameID());
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[FE-FEGR fe=%s fegr=%s]", this.first.getName(), this.second);
	}
}
