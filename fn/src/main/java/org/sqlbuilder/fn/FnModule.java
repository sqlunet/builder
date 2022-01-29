package org.sqlbuilder.fn;

import org.sqlbuilder.common.Module;
import org.sqlbuilder.fn.collectors.*;

import java.io.FileNotFoundException;
import java.io.IOException;

public class FnModule extends Module
{
	public static final String MODULE_ID = "fn";

	protected FnModule(final String conf)
	{
		super(MODULE_ID, conf);
	}

	@Override
	protected void run()
	{
		Inserter inserter = new Inserter(props);
		new FnEnumProcessor().run();
		try
		{
			inserter.insertPreset();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		new FnSemTypeProcessor("semTypes.xml", props).run();
		try
		{
			inserter.insertSemTypes();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		new FnFrameProcessor(props).run();
		try
		{
			inserter.insertFrames();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		new FnLexUnitProcessor(props).run();
		try
		{
			inserter.insertLexUnits();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		new FnFullTextProcessor(props).run();
		try
		{
			inserter.insertFullText();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		try
		{
			inserter.insertFinal();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(final String[] args) throws IOException
	{
		new FnModule(args[0]).run();
	}

	/*
	private static void normalize(final boolean execute) throws SQLException
	{
		System.out.println("\n-- normalize frame reference");
		new Normalizer("Fn_frames", "frame", "frameid") //
				.targets("Fn_frames_related", "frame2", "frame2id") //
				.reference() //
				.cleanup() //
				.exec(execute);

		// framerelations.table=fnframerelations
		// framerelations.create=CREATE TABLE IF NOT EXISTS %Fn_framerelations.table% ( relationid INTEGER NOT NULL AUTO_INCREMENT,relation VARCHAR(20) DEFAULT NULL,PRIMARY KEY (relationid) );
		// framerelations.unq1=CREATE UNIQUE INDEX IF NOT EXISTS unq_%Fn_framerelations.table%_relation ON %Fn_framerelations.table% (relation);
		// framerelations.no-unq1=DROP INDEX IF EXISTS unq_%Fn_framerelations.table%_relation;
		System.out.println("\n-- normalize frame relation reference");
		new Normalizer("Fn_framerelations", "relation", "relationid") //
				.targets("Fn_frames_related", "relation", "relationid") //
				.create() //
				.insert() //
				.reference() //
				.cleanup() //
				.newPk("frameid,frame2id,relationid").exec(execute);

		// fetypes.table=fnfetypes
		// fetypes.create=CREATE TABLE IF NOT EXISTS %Fn_fetypes.table% ( fetypeid INTEGER NOT NULL AUTO_INCREMENT,fetype VARCHAR(30),PRIMARY KEY (fetypeid) );
		// fetypes.unq1=CREATE UNIQUE INDEX IF NOT EXISTS unq_%Fn_fetypes.table%_fetype ON %Fn_fetypes.table% (fetype);
		// fetypes.no-unq1=DROP INDEX IF EXISTS unq_%Fn_fetypes.table%_fetype;
		System.out.println("\n-- normalize fetypes reference");
		new Normalizer("Fn_fetypes", "fetype", "fetypeid") //
				.targets("Fn_fes", "fetype", "fetypeid") //
				.create() //
				.insert() //
				.reference() //
				.cleanup() //
				.targets("Fn_ferealizations", "fetype", "fetypeid") //
				.reference() //
				.cleanup() //
				.targets("Fn_lexunits", "incorporatedfetype", "incorporatedfetypeid") //
				.reference() //
				.cleanup() //
				.targets("Fn_patterns_valenceunits", "fetype", "fetypeid") //
				.reference() //
				.cleanup() //
				.targets("Fn_fegrouprealizations_fes", "fetype", "fetypeid") //
				.reference() //
				.cleanup() //
				.exec(execute);

		// pttypes.table=fnpttypes
		// pttypes.create=CREATE TABLE IF NOT EXISTS %Fn_pttypes.table% ( ptid INTEGER NOT NULL AUTO_INCREMENT,pt VARCHAR(20),PRIMARY KEY (ptid) );
		// pttypes.unq1=CREATE UNIQUE INDEX IF NOT EXISTS unq_%Fn_pttypes.table%_pt ON %Fn_pttypes.table% (pt);
		// pttypes.no-unq1=DROP INDEX IF EXISTS unq_%Fn_pttypes.table%_pt;
		System.out.println("\n-- normalize pt reference");
		new Normalizer("Fn_pttypes", "pt", "ptid") //
				.targets("Fn_valenceunits", "pt", "ptid") //
				.create() //
				.insert() //
				.reference() //
				.cleanup() //
				.exec(execute);

		// gftypes.table=fngftypes
		// gftypes.create=CREATE TABLE IF NOT EXISTS %Fn_gftypes.table% ( gfid INTEGER NOT NULL AUTO_INCREMENT,gf VARCHAR(10),PRIMARY KEY (gfid) );
		// gftypes.unq1=CREATE UNIQUE INDEX IF NOT EXISTS unq_%Fn_gftypes.table%_gf ON %Fn_gftypes.table% (gf);
		// gftypes.no-unq1=DROP INDEX IF EXISTS unq_%Fn_gftypes.table%_gf;
		System.out.println("\n-- normalize gf reference");
		new Normalizer("Fn_gftypes", "gf", "gfid") //
				.targets("Fn_valenceunits", "gf", "gfid") //
				.create() //
				.insert() //
				.reference() //
				.cleanup() //
				.exec(execute);

		// layertypes.table=fnlayertypes
		// layertypes.create=CREATE TABLE IF NOT EXISTS %Fn_layertypes.table% ( layertypeid INTEGER NOT NULL AUTO_INCREMENT,layertype VARCHAR(6),PRIMARY KEY (layertypeid) );
		System.out.println("\n-- normalize layertype reference");
		new Normalizer("Fn_layertypes", "layertype", "layertypeid") //
				.targets("Fn_layers", "layertype", "layertypeid") //
				.create() //
				.insert() //
				.reference() //
				.cleanup() //
				.exec(execute);

		// labeltypes.table=fnlabeltypes
		// labeltypes.create=CREATE TABLE IF NOT EXISTS %Fn_labeltypes.table% ( labeltypeid INTEGER NOT NULL AUTO_INCREMENT,labeltype VARCHAR(26),PRIMARY KEY (labeltypeid) );
		// labeltypes.insert=INSERT INTO %Fn_labeltypes.table% (labeltypeid,labeltype) VALUES(?);
		System.out.println("\n-- normalize labeltype reference");
		new Normalizer("Fn_labeltypes", "labeltype", "labeltypeid") //
				.targets("Fn_labels", "labeltype", "labeltypeid") //
				.create() //
				.insert() //
				.reference() //
				.cleanup() //
				.exec(execute);

		// statuses.table=fnstatuses
		// statuses.create=CREATE TABLE IF NOT EXISTS %Fn_statuses.table% ( statusid INTEGER NOT NULL,status VARCHAR(32),PRIMARY KEY (statusid) );
		System.out.println("\n-- normalize status reference");
		new Normalizer("Fn_statuses", "status", "statusid") //
				.targets("Fn_lexunits", "status", "statusid") //
				.create() //
				.insert() //
				.reference() //
				.cleanup() //
				.targets("Fn_annosets", "status", "statusid") //
				.insert() //
				.reference() //
				.cleanup() //
				.exec(execute);

		// cxns.table=fncxns
		// cxns.create=CREATE TABLE IF NOT EXISTS %Fn_cxns.table% ( cxnid INTEGER NOT NULL,cxn VARCHAR(32),PRIMARY KEY (cxnid) );
		System.out.println("\n-- normalize cxn reference");
		new Normalizer("Fn_cxns", "cxn", "cxnid") //
				.targets("Fn_annosets", "cxn", "cxnid") //
				.create2() //
				.insert2() //
				.cleanup() //
				.exec(execute);

		// infer feid from fetypeid and other data from joined tables

		System.out.println("\n-- normalize ferealizations (feid)");
		new Normalizer("Fn_fes", "fetypeid", "feid") //
				.targets("Fn_ferealizations", "fetypeid", "feid") //
				.referenceThrough("Fn_lexunits", "luid", "Fn_frames", "frameid") //
				.exec(execute);

		System.out.println("\n-- normalize fegrouprealizations (feid)");
		new Normalizer("Fn_fes", "fetypeid", "feid") //
				.targets("Fn_fegrouprealizations_fes", "fetypeid", "feid") //
				.referenceThrough("Fn_fegrouprealizations", "fegrid", "Fn_lexunits", "luid", "Fn_frames", "frameid") //
				.exec(execute);

		System.out.println("\n-- normalize lexunits (incorporatedfeid)");
		new Normalizer("Fn_fes", "fetypeid", "feid") //
				.targets("Fn_lexunits", "incorporatedfetypeid", "incorporatedfeid") //
				.referenceThrough("Fn_frames", "frameid") //
				.exec(execute);

		System.out.println("\n-- normalize patterns_valenceunits (feid)");
		new Normalizer("Fn_fes", "fetypeid", "feid") //
				.targets("Fn_patterns_valenceunits", "fetypeid", "feid") //
				.referenceThrough("Fn_patterns", "patternid", "Fn_fegrouprealizations", "fegrid", "Fn_lexunits", "luid") //
				.exec(execute);
	}
	*/
}
