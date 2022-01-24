package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
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
	public static final Set<FERealization> SET = new HashSet<>();

	public static Map<FERealization, Integer> MAP;

	private final FERealizationType fer;

	private final int luid;

	private final int frameid;

	public FERealization(final FERealizationType fer, final int luid, final int frameid)
	{
		this.fer = fer;
		this.luid = luid;
		this.frameid = frameid;
		SET.add(this);
	}

	// A C C E S S

	public String getFEName()
	{
		return fer.getFE().getName();
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
		Integer id = MAP.get(this);
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
		return fer.equals(that.fer) && luid == that.luid && frameid == that.frameid;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(fer, luid, frameid);
	}

	// O R D E R

	public static final Comparator<FERealization> COMPARATOR = Comparator.comparing(FERealization::getFEName) //
			.thenComparing(FERealization::getLuId) //
			.thenComparing(FERealization::getFrameId);

	// I N S E R T

	@Override
	public String dataRow()
	{
		// ferid INTEGER NOT NULL
		// fetypeid INTEGER NOT NULL
		// feid INTEGER NOT NULL
		// total INTEGER
		// luid INTEGER NOT NULL

		String feName = fer.getFE().getName();
		int fetypeid = FeType.getIntId(feName);
		var key = new Pair<>(fetypeid, frameid);
		var feid = FE.BY_FETYPEID_AND_FRAMEID.get(key).getID();

		return String.format("%s,%s,%s,%d", //
				fetypeid, //
				feid,
				fer.getTotal(), //
				luid);
	}

	@Override
	public String comment()
	{
		return String.format("%s", fer.getFE().getName());
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[FER fe=%s luid=%s]", fer.getFE(), luid);
	}
}
