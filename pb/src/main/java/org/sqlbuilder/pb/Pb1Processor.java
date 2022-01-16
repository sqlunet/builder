package org.sqlbuilder.pb;

import org.sqlbuilder.common.MapFactory;
import org.sqlbuilder.common.Progress;

import java.util.Properties;

public class Pb1Processor extends Pb0Processor
{
	public Pb1Processor(final Properties props)
	{
		super(props, "pb1");
	}

	@Override
	protected void run()
	{
		Progress.traceHeader("propbank pass1", null);
		super.run();
		tailProcessing();
		Progress.traceTailer("propbank pass1", 1);
	}

	protected void tailProcessing()
	{
		// make maps
		Progress.traceHeader(this.tag, "maps");
		PbFunc.MAP = MapFactory.makeMap(PbFunc.SET);
		PbTheta.MAP = MapFactory.makeMap(PbTheta.SET);
		PbExample.aspectMap = MapFactory.makeMap(PbExample.aspectSet);
		PbExample.formMap = MapFactory.makeMap(PbExample.formSet);
		PbExample.personMap = MapFactory.makeMap(PbExample.personSet);
		PbExample.tenseMap = MapFactory.makeMap(PbExample.tenseSet);
		PbExample.voiceMap = MapFactory.makeMap(PbExample.voiceSet);
		Progress.traceTailer("maps", 8);

		// inserts
		Progress.traceHeader(this.tag, "inserting presets");
		Pb1Processor.insertPresets();
		Progress.traceTailer("maps", 1);

		Progress.traceHeader(this.tag, "inserting rolesets");
		insertMap(PbRoleSet.MAP, "rolesets");
		Progress.traceTailer("rolesets", 1);

		Progress.traceHeader(this.tag, "inserting roles");
		insertMap(PbRole.MAP, "roles");
		Progress.traceTailer("roles", 1);
	}

	private static void insertPresets()
	{
		insertSet(PbArg.nSet, PbArg.nNames, "PbArgNType");

		insertMap(PbFunc.MAP, PbFunc.funcNames, "PbFuncType");
		insertMap(PbTheta.MAP, "PbVnTheta");
		insertMap(PbExample.aspectMap, "PbAspectType");
		insertMap(PbExample.formMap, "PbFormType");
		insertMap(PbExample.personMap, "PbPersonType");
		insertMap(PbExample.tenseMap, "PbTenseType");
		insertMap(PbExample.voiceMap, "PbVoiceType");
	}
}
