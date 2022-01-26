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

import edu.berkeley.icsi.framenet.FrameLUType;
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

	private final int luid;

	private final String name;

	private final int pos;

	private final int frameid;

	private final String frameName;

	private final String definition;

	private final Character dict;

	private final String incorporatedFE;

	private final int totalAnnotated;

	public static LexUnit make(final LexUnitDocument.LexUnit lu)
	{
		var u = new LexUnit(lu);

		boolean isNew = SET.add(u);
		if (!isNew)
		{
			Logger.instance.logWarn(FnModule.MODULE_ID, "LexUnit", "lu-duplicate", null, -1, null, u.toString());
		}
		return u;
	}

	public static LexUnit make(final FrameLUType lu, final int frameid, final String frameName)
	{
		var u = new LexUnit(lu, frameid, frameName);

		boolean isNew = SET.add(u);
		if (!isNew)
		{
			Logger.instance.logWarn(FnModule.MODULE_ID, "LexUnit", "frame_lu-duplicate", null, -1, null, u.toString());
		}
		return u;
	}

	private LexUnit(final LexUnitDocument.LexUnit lu)
	{
		this.luid = lu.getID();
		this.name = lu.getName();
		this.pos = lu.getPOS().intValue();
		final Definition def = Definition.getDefinition(lu.getDefinition());
		this.definition = def.def;
		this.dict = def.dict;
		this.incorporatedFE = lu.getIncorporatedFE();
		this.totalAnnotated = lu.getTotalAnnotated();
		this.frameid = lu.getFrameID();
		this.frameName = lu.getFrame();
	}

	private LexUnit(final FrameLUType lu, final int frameid, final String frameName)
	{
		this.luid = lu.getID();
		this.name = lu.getName();
		this.pos = lu.getPOS().intValue();
		final Definition def = Definition.getDefinition(lu.getDefinition());
		this.definition = def.def;
		this.dict = def.dict;
		this.incorporatedFE = lu.getIncorporatedFE();
		this.totalAnnotated = 0;
		this.frameid = frameid;
		this.frameName = frameName;
	}

	// A C C E S S

	public int getID()
	{
		return luid;
	}

	public String getName()
	{
		return name;
	}

	public String getFrameName()
	{
		return frameName;
	}

	public int getFrameID()
	{
		return frameid;
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
		LexUnit that = (LexUnit) o;
		return luid == that.luid;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(luid);
	}

	// O R D E R

	public static Comparator<LexUnit> COMPARATOR = Comparator.comparing(LexUnit::getName).thenComparing(LexUnit::getID);

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("%d,'%s',%d,%s,%s,%s,%d,%d", //
				luid, //
				Utils.escape(name), //
				pos, //
				Utils.nullableString(definition), //
				Utils.nullableChar(dict), //
				FeType.getId(incorporatedFE), //
				totalAnnotated, //
				frameid); //
	}

	@Override
	public String comment()
	{
		return String.format("%d,%s,%d,%s", luid, name, frameid, frameName);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[LU luid=%d lu=%s frameid=%d frame=%s]", luid, name, frameid, frameName);
	}

	// D E F I N I T I O N

	public static class Definition
	{
		public final Character dict;

		public final String def;

		public Definition(final Character dict, final String definition)
		{
			super();
			this.dict = dict;
			this.def = definition;
		}

		public static Definition getDefinition(final String definition0)
		{
			Character dict = null;
			String definition = definition0;
			if (definition0.startsWith("COD"))
			{
				dict = 'O';
				definition = definition0.substring(3);
			}
			if (definition0.startsWith("FN"))
			{
				dict = 'F';
				definition = definition0.substring(2);
			}
			// noinspection ConstantConditions
			if (definition != null)
			{
				definition = definition.replaceAll("[ \t\n.:]*$|^[ \t\n.:]*", "");
			}
			return new Definition(dict, definition);
		}

		@Override
		public String toString()
		{
			return this.dict + "|<" + this.def + ">";
		}
	}
}
