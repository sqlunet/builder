package org.sqlbuilder.pb;

import org.sqlbuilder.common.Module;
import org.sqlbuilder.pb.collectors.*;

import java.io.IOException;
import java.util.Properties;

public class PbModule extends Module
{
	public static final String MODULE_ID = "pb";

	protected PbModule(final String conf)
	{
		super(MODULE_ID, conf);
	}

	@Override
	protected void run()
	{
		try
		{
			buildPropBank(props);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static void buildPropBank(final Properties props) throws IOException
	{
		new PbProcessor(props).run();
	}

	private static void initPropBankBase(final Properties props)
	{
		new PbProcessor(props).run();
	}

	private static void initSemLink(final Properties props)
	{
		new Semlink0Processor(props).run();
	}

	private static void buildSemLink(final Properties props)
	{
		// semlink and propbank data
		PbModule.initSemLink(props);
		PbModule.initPropBankBase(props);

		// process
		new SemlinkProcessor().run();
	}

	private static void buildSemLink1(final Properties props)
	{
		// semlink and propbank data
		PbModule.initSemLink(props);
		PbModule.initPropBankBase(props);

		// process
		new Semlink1Processor().run();
	}

	private static void buildSemLink2(final Properties props)
	{
		// semlink and propbank data
		PbModule.initSemLink(props);
		PbModule.initPropBankBase(props);

		// process
		new Semlink2Processor().run();
	}

	private static void buildXRefs(final Properties props)
	{
		// retrieve propbank data
		PbModule.initPropBankBase(props);

		// process
		new PbCrossRefsProcessor(props).run();
	}

	public static void main(final String[] args)
	{
		new PbModule(args[0]).run();
		Inserter.insert();
	}
}
