package org.sqlbuilder.fn;

import org.sqlbuilder.common.Module;
import org.sqlbuilder.fn.collectors.FnFrameProcessor;
import org.sqlbuilder.fn.collectors.FnLexUnitProcessor;
import org.sqlbuilder.fn.collectors.FnPresetProcessor;
import org.sqlbuilder.fn.collectors.FnSemTypeProcessor;

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
		new FnPresetProcessor().run();
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

		//new FnFullTextProcessor(props).run();
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

		System.out.println("\n-- normalize frame relation reference");
		new Normalizer("Fn_framerelations", "relation", "relationid") //
				.targets("Fn_frames_related", "relation", "relationid") //
				.create() //
				.insert() //
				.reference() //
				.cleanup() //
				.newPk("frameid,frame2id,relationid").exec(execute);

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

		System.out.println("\n-- normalize pt reference");
		new Normalizer("Fn_pttypes", "pt", "ptid") //
				.targets("Fn_valenceunits", "pt", "ptid") //
				.create() //
				.insert() //
				.reference() //
				.cleanup() //
				.exec(execute);

		System.out.println("\n-- normalize gf reference");
		new Normalizer("Fn_gftypes", "gf", "gfid") //
				.targets("Fn_valenceunits", "gf", "gfid") //
				.create() //
				.insert() //
				.reference() //
				.cleanup() //
				.exec(execute);

		System.out.println("\n-- normalize layertype reference");
		new Normalizer("Fn_layertypes", "layertype", "layertypeid") //
				.targets("Fn_layers", "layertype", "layertypeid") //
				.create() //
				.insert() //
				.reference() //
				.cleanup() //
				.exec(execute);

		System.out.println("\n-- normalize labeltype reference");
		new Normalizer("Fn_labeltypes", "labeltype", "labeltypeid") //
				.targets("Fn_labels", "labeltype", "labeltypeid") //
				.create() //
				.insert() //
				.reference() //
				.cleanup() //
				.exec(execute);

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
