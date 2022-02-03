package org.sqlbuilder.pb;

import org.sqlbuilder.common.MapFactory;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.pb.objects.*;

import java.io.File;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class Inserter
{
	private final File outDir;

	public Inserter(final Properties conf)
	{
		this.outDir = new File(conf.getProperty("vnoutdir", "sql/data"));
		if (!this.outDir.exists())
		{
			this.outDir.mkdirs();
		}
	}

	private static void makeMaps()
	{
		// roles
		Role.MAP = MapFactory.makeMap(Role.SET);
		Progress.traceTailer("roles",  Long.toString(Role.MAP.size()));

		// rolesets
		RoleSet.MAP = MapFactory.makeMap(RoleSet.SET);
		Progress.traceTailer("rolesets",  Long.toString(RoleSet.MAP.size()));

		Func.MAP = MapFactory.makeMap(Func.SET);
		Progress.traceTailer("func",  Long.toString(Func.MAP .size()));

		PbTheta.MAP = MapFactory.makeMap(PbTheta.SET);
		Progress.traceTailer("theta", Long.toString(PbTheta.MAP .size()));

		Example.aspectMap = MapFactory.makeMap(Example.aspectSet);
		Progress.traceTailer("aspect",  Long.toString(Example.aspectMap.size()));

		Example.formMap = MapFactory.makeMap(Example.formSet);
		Progress.traceTailer("form",  Long.toString(Example.formMap.size()));

		Example.personMap = MapFactory.makeMap(Example.personSet);
		Progress.traceTailer("person",  Long.toString(Example.personMap.size()));

		Example.tenseMap = MapFactory.makeMap(Example.tenseSet);
		Progress.traceTailer("tense", Long.toString(Example.tenseMap.size()));

		Example.voiceMap = MapFactory.makeMap(Example.voiceSet);
		Progress.traceTailer("voice",  Long.toString(Example.voiceMap.size()));

		// examples
		//Progress.traceHeader("examples", "map");
		//PbExample.MAP = MapFactory.makeMap(PbExample.SET);
		//Progress.traceTailer("examples", PbExample.MAP.size());
	}

	public static void insert()
	{
		Progress.traceHeader("maps", "making");
		makeMaps();
		Progress.traceTailer("maps", "done");

		insertMap(RoleSet.MAP, "pb_rolesets");
		insertMap(Role.MAP, "pb_roles");
		insertSet(Example.SET, "pb_examples");
		insertSet(Arg.SET, "pb_args");
		insertSet(Rel.SET, "pb_rels");
		insertSet(RoleSetMember.SET, "pb_members");

		insertSet(Arg.nSet, Arg.nNames, "pb_argntype");
		insertMap(Example.aspectMap, "pb_aspecttype");
		insertMap(Example.formMap, "pb_formtype");
		insertMap(Example.personMap, "pb_persontype");
		insertMap(Example.tenseMap, "pb_tensetype");
		insertMap(Example.voiceMap, "pb_voicetype");

		insertMap(Func.MAP, Func.funcNames, "pb_functype");
		insertMap(PbTheta.MAP, "pbvn_theta");
	}

	protected static <T> void insertSet(final Set<T> set, final String table)
	{
		Progress.traceTailer(table, "set: " + Long.toString(set.size()));
	}

	protected static <T> void insertSet(final Set<T> set, final Properties props, final String table)
	{
		Progress.traceTailer(table, "set: " + Long.toString(set.size()));
	}

	protected static <T> void insertMap(final Map<T, Integer> map, final String table)
	{
		Progress.traceTailer(table, "map: " + Long.toString(map.size()));
	}

	protected static <T> void insertMap(final Map<T, Integer> map, final Properties props, final String table)
	{
		Progress.traceTailer(table, "map: " + Long.toString(map.size()));
	}
}

