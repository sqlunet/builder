package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.HasID;
import org.sqlbuilder.fn.collectors.FnFEXmlProcessor;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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
	}

	public long getID()
	{
		return fe.getID();
	}

	@Override
	public String dataRow()
	{
		return String.format("%d,'%s','%s','%s',%d,%s,%d", //
				fe.getID(), //
				Utils.escape(fe.getName()), //
				Utils.escape(fe.getAbbrev()), //
				Utils.escape(definition), //
				fe.getCoreType().intValue(), //
				coreset == null ? "NULL" : coreset, //
				frameid); //
		//String(8, this.fe.getFgColor());
		//String(9, this.fe.getBgColor());
		//String(10, this.fe.getCDate());
		//String(11, this.fe.getCBy());
	}

	@Override
	public String toString()
	{
		return String.format("[FE feid=%s name=%s frameid=%s]", fe.getID(), fe.getName(), frameid);
	}
}
