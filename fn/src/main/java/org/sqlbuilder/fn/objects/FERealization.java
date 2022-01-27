package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.Collector;
import org.sqlbuilder.fn.HasId;
import org.sqlbuilder.fn.joins.Pair;
import org.sqlbuilder.fn.types.FeType;

import java.util.*;

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
	public static final Comparator<FERealization> COMPARATOR = Comparator //
			.comparing(FERealization::getFEName) //
			.thenComparing(FERealization::getLuId) //
			.thenComparing(FERealization::getFrameId);

	public static final Collector<FERealization> COLLECTOR = new Collector<>(COMPARATOR);

	private final String feName;

	private final int total;

	private final int luid;

	private final int frameid;

	public static FERealization make(final FERealizationType fer, final int luid, final int frameid)
	{
		var r = new FERealization(fer, luid, frameid);
		COLLECTOR.add(r);
		return r;
	}

	private FERealization(final FERealizationType fer, final int luid, final int frameid)
	{
		this.feName = fer.getFE().getName();
		this.total = fer.getTotal();
		this.luid = luid;
		this.frameid = frameid;
	}

	// A C C E S S

	public String getFEName()
	{
		return feName;
	}

	public int getLuId()
	{
		return luid;
	}

	public int getFrameId()
	{
		return frameid;
	}

	@Override
	public Object getId()
	{
		Integer id = COLLECTOR.get(this);
		if (id != null)
		{
			return id;
		}
		return "NULL";
	}

	// I D E N T I T Y

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		FERealization that = (FERealization) o;
		return feName.equals(that.feName) && luid == that.luid && frameid == that.frameid;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(feName, luid, frameid);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		// ferid INTEGER NOT NULL
		// fetypeid INTEGER NOT NULL
		// feid INTEGER NOT NULL
		// total INTEGER
		// luid INTEGER NOT NULL

		int fetypeid = FeType.getIntId(feName);
		var key = new Pair<>(fetypeid, frameid);
		var feid = FE.BY_FETYPEID_AND_FRAMEID.get(key).getID();

		return String.format("%s,%s,%s,%d", //
				fetypeid, //
				feid, total, //
				luid);
	}

	@Override
	public String comment()
	{
		return String.format("%s", feName);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[FER fe=%s luid=%s]", feName, luid);
	}
}
