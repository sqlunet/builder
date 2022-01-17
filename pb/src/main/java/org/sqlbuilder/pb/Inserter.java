package org.sqlbuilder.pb;

import org.sqlbuilder.common.MapFactory;
import org.sqlbuilder.common.Progress;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class Inserter
{
	private static void makeMaps()
	{
		// roles
		PbRole.MAP = MapFactory.makeMap(PbRole.SET);
		Progress.traceTailer("roles",  Long.toString(PbRole.MAP.size()));

		// rolesets
		PbRoleSet.MAP = MapFactory.makeMap(PbRoleSet.SET);
		Progress.traceTailer("rolesets",  Long.toString(PbRoleSet.MAP.size()));

		PbFunc.MAP = MapFactory.makeMap(PbFunc.SET);
		Progress.traceTailer("func",  Long.toString(PbFunc.MAP .size()));

		PbTheta.MAP = MapFactory.makeMap(PbTheta.SET);
		Progress.traceTailer("theta", Long.toString(PbTheta.MAP .size()));

		PbExample.aspectMap = MapFactory.makeMap(PbExample.aspectSet);
		Progress.traceTailer("aspect",  Long.toString(PbExample.aspectMap.size()));

		PbExample.formMap = MapFactory.makeMap(PbExample.formSet);
		Progress.traceTailer("form",  Long.toString(PbExample.formMap.size()));

		PbExample.personMap = MapFactory.makeMap(PbExample.personSet);
		Progress.traceTailer("person",  Long.toString(PbExample.personMap.size()));

		PbExample.tenseMap = MapFactory.makeMap(PbExample.tenseSet);
		Progress.traceTailer("tense", Long.toString(PbExample.tenseMap.size()));

		PbExample.voiceMap = MapFactory.makeMap(PbExample.voiceSet);
		Progress.traceTailer("voice",  Long.toString(PbExample.voiceMap.size()));

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

		insertMap(PbRoleSet.MAP, "pb_rolesets");
		insertMap(PbRole.MAP, "pb_roles");
		insertSet(PbExample.SET, "pb_examples");
		insertSet(PbArg.SET, "pb_args");
		insertSet(PbRel.SET, "pb_rels");
		insertSet(PbRoleSetMember.SET, "pb_members");

		insertSet(PbArg.nSet, PbArg.nNames, "pb_argntype");
		insertMap(PbExample.aspectMap, "pb_aspecttype");
		insertMap(PbExample.formMap, "pb_formtype");
		insertMap(PbExample.personMap, "pb_persontype");
		insertMap(PbExample.tenseMap, "pb_tensetype");
		insertMap(PbExample.voiceMap, "pb_voicetype");

		insertMap(PbFunc.MAP, PbFunc.funcNames, "pb_functype");
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

