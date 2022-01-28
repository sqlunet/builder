package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.HasID;
import org.sqlbuilder.fn.RequiresIdFrom;
import org.sqlbuilder.fn.collectors.FnFEXmlProcessor;
import org.sqlbuilder.fn.joins.Pair;
import org.sqlbuilder.fn.types.FeType;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import edu.berkeley.icsi.framenet.FEType;

/*
fes.table=fnfes
fes.create=CREATE TABLE IF NOT EXISTS %Fn_fes.table% ( feid INTEGER NOT NULL,frameid INTEGER,fetypeid INTEGER DEFAULT NULL,feabbrev VARCHAR(24),fedefinition TEXT,coretypeid INTEGER DEFAULT NULL,coreset INTEGER DEFAULT NULL,fgcolor VARCHAR(6),bgcolor VARCHAR(6),cdate VARCHAR(27),cby VARCHAR(5),PRIMARY KEY (feid) );
fes.altcreate1=ALTER TABLE %Fn_fes.table% ADD COLUMN fetype VARCHAR(30) AFTER fetypeid;
fes.index1=CREATE INDEX IF NOT EXISTS k_%Fn_fes.table%_frameid ON %Fn_fes.table% (frameid);
fes.no-index1=DROP INDEX IF EXISTS k_%Fn_fes.table%_frameid;
fes.index2=CREATE INDEX IF NOT EXISTS k_%Fn_fes.table%_fetypeid ON %Fn_fes.table% (fetypeid);
fes.no-index2=DROP INDEX IF EXISTS k_%Fn_fes.table%_fetypeid;
fes.fk1=ALTER TABLE %Fn_fes.table% ADD CONSTRAINT fk_%Fn_fes.table%_frameid FOREIGN KEY (frameid) REFERENCES %Fn_frames.table% (frameid);
fes.fk2=ALTER TABLE %Fn_fes.table% ADD CONSTRAINT fk_%Fn_fes.table%_fetypeid FOREIGN KEY (fetypeid) REFERENCES %Fn_fetypes.table% (fetypeid);
fes.fk3=ALTER TABLE %Fn_fes.table% ADD CONSTRAINT fk_%Fn_fes.table%_coretypeid FOREIGN KEY (coretypeid) REFERENCES %Fn_coretypes.table% (coretypeid);
fes.no-fk1=ALTER TABLE %Fn_fes.table% DROP CONSTRAINT fk_%Fn_fes.table%_coretypeid CASCADE;
fes.no-fk2=ALTER TABLE %Fn_fes.table% DROP CONSTRAINT fk_%Fn_fes.table%_fetypeid CASCADE;
fes.no-fk3=ALTER TABLE %Fn_fes.table% DROP CONSTRAINT fk_%Fn_fes.table%_frameid CASCADE;
fes.insert=INSERT INTO %Fn_fes.table% (feid,frameid,fetype,feabbrev,fedefinition,coretypeid,coreset,fgcolor,bgcolor,cdate,cby) VALUES(?,?,?,?,?,?,?,?,?,?,?);
 */
public class FE implements HasID, Insertable<FE>
{
	public static final Set<FE> SET = new HashSet<>();

	public static Map<Pair<Integer, Integer>, FE> BY_FETYPEID_AND_FRAMEID;

	private static final FnFEXmlProcessor definitionProcessor = new FnFEXmlProcessor();

	private final int feid;

	public final String name;

	public final String abbrev;

	private final int coretype;

	private final Integer coreset;

	public final String definition;

	private final int frameid;

	public static FE make(final FEType fe, final Integer coreset, final int frameid) throws ParserConfigurationException, IOException, SAXException
	{
		var e = new FE(fe, coreset, frameid);
		SET.add(e);
		return e;
	}

	private FE(final FEType fe, final Integer coreset, final int frameid) throws ParserConfigurationException, IOException, SAXException
	{
		this.feid = fe.getID();
		this.name = fe.getName();
		this.abbrev = fe.getAbbrev();
		this.coretype = fe.getCoreType().intValue();
		this.coreset = coreset;
		this.frameid = frameid;
		try
		{
			this.definition = FE.definitionProcessor.process(fe.getDefinition());
		}
		catch (ParserConfigurationException | SAXException | IOException e)
		{
			System.err.println(fe.getDefinition());
			throw e;
		}
		FeType.COLLECTOR.add(fe.getName());
	}

	// A C C E S S

	public long getID()
	{
		return feid;
	}

	public String getName()
	{
		return name;
	}

	public String getAbbrev()
	{
		return abbrev;
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
		FE that = (FE) o;
		return feid == that.feid;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(feid);
	}

	// O R D E R

	public static Comparator<FE> COMPARATOR = Comparator.comparing(FE::getName).thenComparing(FE::getID);

	// I N S E R T

	@RequiresIdFrom(type = FeType.class)
	@Override
	public String dataRow()
	{
		// (feid,fetypeid,feabbrev,fedefinition,coretypeid,coreset,frameid)
		return String.format("%d,%s,'%s','%s',%d,%s,%d", //
				feid, //
				FeType.getSqlId(name), //
				Utils.escape(abbrev), //
				Utils.escape(definition), //
				coretype, //
				Utils.nullableInt(coreset), //
				frameid); //
		// String(8, this.fe.getFgColor());
		// String(9, this.fe.getBgColor());
		// String(10, this.fe.getCDate());
		// String(11, this.fe.getCBy());
	}

	@Override
	public String comment()
	{
		return String.format("%s", name);
	}

	@Override
	public String toString()
	{
		return String.format("[FE feid=%s name=%s frameid=%s]", feid, name, frameid);
	}
}
