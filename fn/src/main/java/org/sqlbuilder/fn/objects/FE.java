package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.HasID;
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

	public static Map<Pair<Integer,Integer>, FE> BY_FETYPEID_AND_FRAMEID;

	private static final FnFEXmlProcessor definitionProcessor = new FnFEXmlProcessor();

	private final FEType fe;

	private final Integer coreset;

	public final String definition;

	private final int frameid;

	public FE(final FEType fe, final Integer coreset, final int frameid) throws ParserConfigurationException, IOException, SAXException
	{
		this.fe = fe;
		this.coreset = coreset;
		try
		{
			this.definition = FE.definitionProcessor.process(this.fe.getDefinition());
		}
		catch (ParserConfigurationException | SAXException | IOException e)
		{
			System.err.println(this.fe.getDefinition());
			throw e;
		}
		this.frameid = frameid;
		FeType.add(fe.getName());
		SET.add(this);
	}
	// A C C E S S

	public long getID()
	{
		return fe.getID();
	}

	public String getName()
	{
		return fe.getName();
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
		FE fe1 = (FE) o;
		return fe.equals(fe1.fe) && frameid == fe1.frameid && Objects.equals(coreset, fe1.coreset) && definition.equals(fe1.definition);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(fe, frameid, coreset, definition);
	}

	// O R D E R

	public static Comparator<FE> COMPARATOR = Comparator.comparing(FE::getName).thenComparing(FE::getID);

	// I N S E R T

	@Override
	public String dataRow()
	{
		// (feid,fetypeid,feabbrev,fedefinition,coretypeid,coreset,frameid)
		// feid INTEGER NOT NULL,
		// fetypeid INTEGER DEFAULT NULL,
		// feabbrev VARCHAR(24),
		// fedefinition TEXT,
		// coretypeid INTEGER DEFAULT NULL,
		// coreset INTEGER DEFAULT NULL,
		// fgcolor VARCHAR(6),bgcolor VARCHAR(6),cdate VARCHAR(27),cby VARCHAR(5)
		// frameid INTEGER,
		return String.format("%d,%s,'%s','%s',%d,%s,%d", //
				fe.getID(), //
				FeType.getId(fe.getName()), //
				Utils.escape(fe.getAbbrev()), //
				Utils.escape(definition), //
				fe.getCoreType().intValue(), //
				Utils.nullableInt(coreset), //
				frameid); //
		//String(8, this.fe.getFgColor());
		//String(9, this.fe.getBgColor());
		//String(10, this.fe.getCDate());
		//String(11, this.fe.getCBy());
	}

	@Override
	public String comment()
	{
		return String.format("%s", fe.getName());
	}

	@Override
	public String toString()
	{
		return String.format("[FE feid=%s name=%s frameid=%s]", fe.getID(), fe.getName(), frameid);
	}
}
