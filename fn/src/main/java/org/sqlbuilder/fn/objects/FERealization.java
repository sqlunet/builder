package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.HasId;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.berkeley.icsi.framenet.FERealizationType;

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
public class FERealization implements HasId, Insertable<FERealization>
{
	public static final Set<FERealization> SET = new HashSet<>();

	public static Map<FERealization, Integer> MAP;

	private final FERealizationType fer;

	private final int luid;

	public FERealization(final FERealizationType fer, final int luid)
	{
		this.fer = fer;
		this.luid = luid;
	}

	@Override
	public Object getId()
	{
		Integer id = MAP.get(this);
		if (id != null)
		{
			return id;
		}
		return "NULL";
	}

	@Override
	public String dataRow()
	{
		return String.format("%s,'%s',%s,%d", //
				getId(), // fer.getId()
				fer.getFE().getName(), fer.getTotal(), //
				luid);
	}

	@Override
	public String toString()
	{
		return String.format("[FER fe=%s luid=%s]", fer.getFE(), luid);
	}
}
