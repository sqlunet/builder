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
}
