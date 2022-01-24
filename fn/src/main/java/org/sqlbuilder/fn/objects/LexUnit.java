package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.FnModule;
import org.sqlbuilder.fn.HasID;
import org.sqlbuilder.fn.types.FeType;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import edu.berkeley.icsi.framenet.LexUnitDocument;

/*
lexunits.table=fnlexunits
lexunits.create=CREATE TABLE IF NOT EXISTS %Fn_lexunits.table% ( luid INTEGER NOT NULL,frameid INTEGER,lexunit VARCHAR(64),posid INTEGER,ludefinition TEXT,ludict CHARACTER,statusid INTEGER DEFAULT NULL,totalannotated INTEGER,incorporatedfeid INTEGER DEFAULT NULL,incorporatedfetypeid INTEGER DEFAULT NULL,noccurs INTEGER DEFAULT 1,PRIMARY KEY (luid) );
lexunits.altcreate1=ALTER TABLE %Fn_lexunits.table% ADD COLUMN incorporatedfetype VARCHAR(30) AFTER incorporatedfeid;
lexunits.altcreate2=ALTER TABLE %Fn_lexunits.table% ADD COLUMN status VARCHAR(32) AFTER statusid;
lexunits.index1=CREATE INDEX IF NOT EXISTS k_%Fn_lexunits.table%_frameid ON %Fn_lexunits.table% (frameid);
lexunits.no-index1=DROP INDEX IF EXISTS k_%Fn_lexunits.table%_frameid;
lexunits.fk1=ALTER TABLE %Fn_lexunits.table% ADD CONSTRAINT fk_%Fn_lexunits.table%_frameid FOREIGN KEY (frameid) REFERENCES %Fn_frames.table% (frameid);
lexunits.fk2=ALTER TABLE %Fn_lexunits.table% ADD CONSTRAINT fk_%Fn_lexunits.table%_posid FOREIGN KEY (posid) REFERENCES %Fn_poses.table% (posid);
lexunits.fk3=ALTER TABLE %Fn_lexunits.table% ADD CONSTRAINT fk_%Fn_lexunits.table%_incorporatedfeid FOREIGN KEY (incorporatedfeid) REFERENCES %Fn_fes.table% (feid);
lexunits.no-fk1=ALTER TABLE %Fn_lexunits.table% DROP CONSTRAINT fk_%Fn_lexunits.table%_frameid CASCADE;
lexunits.no-fk2=ALTER TABLE %Fn_lexunits.table% DROP CONSTRAINT fk_%Fn_lexunits.table%_incorporatedfeid CASCADE;
lexunits.no-fk3=ALTER TABLE %Fn_lexunits.table% DROP CONSTRAINT fk_%Fn_lexunits.table%_posid CASCADE;
lexunits.insert=INSERT INTO %Fn_lexunits.table% (luid,frameid,lexunit,posid,ludefinition,ludict,status,incorporatedfetype,totalannotated) VALUES(?,?,?,?,?,?,?,?,?);
lexunits.insert2=INSERT INTO %Fn_lexunits.table% (luid,frameid,lexunit,posid,ludefinition,ludict,status,incorporatedfetype,totalannotated) VALUES(?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE noccurs=noccurs+1;
 */
public class LexUnit implements HasID, Insertable<LexUnit>
{
	public static final Set<LexUnit> SET = new HashSet<>();

	public final LexUnitDocument.LexUnit lu;

	public LexUnit(final LexUnitDocument.LexUnit lu)
	{
		super();
		this.lu = lu;
		boolean isNew = SET.add(this);
		if (!isNew)
		{
			Logger.instance.logWarn(FnModule.MODULE_ID, "LexUnit", "lu-duplicate", null, -1, null, toString());
		}
	}

	// A C C E S S

	public int getID()
	{
		return lu.getID();
	}

	public String getName()
	{
		return lu.getName();
	}

	public String getFrameName()
	{
		return lu.getFrame();
	}

	public int getFrameID()
	{
		return lu.getFrameID();
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
		LexUnit lexUnit = (LexUnit) o;
		return lu.equals(lexUnit.lu);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(lu);
	}

	// O R D E R

	public static Comparator<LexUnit> COMPARATOR = Comparator.comparing(LexUnit::getName).thenComparing(LexUnit::getID);

	// I N S E R T

	@Override
	public String dataRow()
	{
		final Definition definition = Definition.getDefinition(this.lu.getDefinition());

		return String.format("%d,'%s',%d,%s,%s,'%s',%s,%d,%d", //
				lu.getID(), //
				Utils.escape(lu.getName()), //
				lu.getPOS().intValue(), //
				Utils.nullableString(definition.def), //
				Utils.nullableChar(definition.dict), //
				lu.getStatus(), //
				FeType.getId(Utils.nullableString(lu.getIncorporatedFE())), //
				lu.getTotalAnnotated(), //
				lu.getFrameID()); //
	}

	@Override
	public String toString()
	{
		return String.format("[LU luid=%d lu=%s frame=%s frameid=%d]", lu.getID(), lu.getName(), lu.getFrame(), lu.getFrameID());
	}
}
