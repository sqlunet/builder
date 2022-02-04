package org.sqlbuilder.pb;

import org.sqlbuilder.common.Insert;
import org.sqlbuilder.common.Names;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.common.ProvidesIdTo;
import org.sqlbuilder.pb.objects.*;

import java.io.File;
import java.io.FileNotFoundException;
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
		// examples
		//Progress.traceHeader("examples", "map");
		//PbExample.MAP = MapFactory.makeMap(PbExample.SET);
		//Progress.traceTailer("examples", PbExample.MAP.size());
	}

	public void insert() throws FileNotFoundException
	{
		Progress.traceHeader("maps", "making");
		makeMaps();
		Progress.traceTailer("maps", "done");

		try ( //
		      @ProvidesIdTo(type = RoleSet.class) var ignored1 = RoleSet.COLLECTOR.open(); //
		      @ProvidesIdTo(type = Role.class) var ignored2 = Role.COLLECTOR.open(); //
		      var ignored3 = Example.ASPECT_COLLECTOR.open(); //
		      var ignored4 = Example.FORM_COLLECTOR.open(); //
		      var ignored5 = Example.PERSON_COLLECTOR.open(); //
		      var ignored6 = Example.TENSE_COLLECTOR.open(); //
		      var ignored7 = Example.VOICE_COLLECTOR.open(); //
		      @ProvidesIdTo(type = Func.class) var ignored8 = Func.COLLECTOR.open(); //
		      @ProvidesIdTo(type = Theta.class) var ignored9 = Theta.COLLECTOR.open(); //
		      @ProvidesIdTo(type = Example.class) var ignored10 = Example.COLLECTOR.open(); //
		      @ProvidesIdTo(type = Arg.class) var ignored11 = Arg.COLLECTOR.open(); //

		      @ProvidesIdTo(type = PbWord.class) var ignored19 = PbWord.COLLECTOR.open() //
		)
		{
			Insert.insert(RoleSet.COLLECTOR, new File(outDir, Names.file("rolesets")), Names.table("rolesets"), Names.columns("rolesets"));
			Insert.insert(Role.COLLECTOR,  new File(outDir, Names.file("roles")), Names.table("roles"), Names.columns("roles"));
			Insert.insert(Example.COLLECTOR, new File(outDir, Names.file("examples")), Names.table("examples"), Names.columns("examples"));
			Insert.insert(Arg.COLLECTOR, new File(outDir, Names.file("args")), Names.table("args"), Names.columns("args"));
			Insert.insert(Rel.COLLECTOR, new File(outDir, Names.file("rels")), Names.table("rels"), Names.columns("rels"));
			Insert.insert(RoleSetMember.SET, null, new File(outDir, Names.file("members")), Names.table("members"), Names.columns("members"));

			insertMap(Arg.N_COLLECTOR, Arg.nNames, new File(outDir, Names.file("argns")), Names.table("argns"), Names.columns("argns"));
			insertMap(Func.COLLECTOR, Func.funcNames, new File(outDir, Names.file("funcs")), Names.table("funcs"), Names.columns("funcs"));
			Insert.insert(Theta.COLLECTOR, new File(outDir, Names.file("thetas")), Names.table("thetas"), Names.columns("thetas"));

			Insert.insertStringMap(Example.ASPECT_COLLECTOR, new File(outDir, Names.file("aspects")), Names.table("aspects"), Names.columns("aspects"));
			Insert.insertStringMap(Example.FORM_COLLECTOR, new File(outDir, Names.file("forms")), Names.table("forms"), Names.columns("forms"));
			Insert.insertStringMap(Example.PERSON_COLLECTOR, new File(outDir, Names.file("persons")), Names.table("persons"), Names.columns("persons"));
			Insert.insertStringMap(Example.TENSE_COLLECTOR, new File(outDir, Names.file("tenses")), Names.table("tenses"), Names.columns("tenses"));
			Insert.insertStringMap(Example.VOICE_COLLECTOR, new File(outDir, Names.file("voices")), Names.table("voices"), Names.columns("voices"));
		}
	}

	protected static <T> void insertMap(final Map<T, Integer> map, final Properties props, final File file, final String... table)
	{
		Progress.traceTailer(table[0], "map: " + Long.toString(map.size()));
	}
}

