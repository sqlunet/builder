package org.sqlbuilder.vn;

import org.sqlbuilder.common.Module;
import org.sqlbuilder.vn.collector.VnProcessor;

import java.io.IOException;

public class VnModule extends Module
{
	public static final String MODULE_ID = "vn";

	public VnModule(final String conf, final Mode mode)
	{
		super(MODULE_ID, conf, mode);
	}

	@Override
	protected void run()
	{
		try
		{
			new VnProcessor(props).run();
			new Inserter(props).insert();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(final String[] args) throws IOException
	{
		int i = 0;
		Mode mode = Mode.PLAIN;
		if (args[i].startsWith("-"))
		{
			mode = Mode.read(args[i++]);
		}
		String conf = args[i];
		new VnModule(conf, mode).run();
	}
}
