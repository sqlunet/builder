package org.sqlbuilder.pb;

import org.sqlbuilder.common.Insert;
import org.sqlbuilder.common.Names;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.common.ProvidesIdTo;
import org.sqlbuilder.pb.foreign.FnAlias;
import org.sqlbuilder.pb.foreign.VnAlias;
import org.sqlbuilder.pb.joins.PbRole_VnRole;
import org.sqlbuilder.pb.objects.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Properties;

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
		Progress.traceHeader("maps", "making");
		Progress.traceTailer("maps", "done");
	}

	public void insert() throws FileNotFoundException
	{
		makeMaps();

		Progress.traceHeader("inserts", "inserting");
		try ( //
		      var ignored1 = Example.ASPECT_COLLECTOR.open(); //
		      var ignored2 = Example.FORM_COLLECTOR.open(); //
		      var ignored3 = Example.PERSON_COLLECTOR.open(); //
		      var ignored4 = Example.TENSE_COLLECTOR.open(); //
		      var ignored5 = Example.VOICE_COLLECTOR.open(); //
		      @ProvidesIdTo(type = Func.class) var ignored6 = Func.COLLECTOR.open(); //
		      @ProvidesIdTo(type = Theta.class) var ignored8 = Theta.COLLECTOR.open(); //

		      @ProvidesIdTo(type = RoleSet.class) var ignored10 = RoleSet.COLLECTOR.open(); //
		      @ProvidesIdTo(type = Role.class) var ignored11 = Role.COLLECTOR.open(); //
		      @ProvidesIdTo(type = Example.class) var ignored14 = Example.COLLECTOR.open(); //
		      @ProvidesIdTo(type = Rel.class) var ignored15 = Rel.COLLECTOR.open(); //
		      @ProvidesIdTo(type = Arg.class) var ignored16 = Arg.COLLECTOR.open(); //

		      @ProvidesIdTo(type = PbWord.class) var ignored20 = PbWord.COLLECTOR.open() //
		)
		{
			Insert.insertStringMap(Example.ASPECT_COLLECTOR, new File(outDir, Names.file("aspects")), Names.table("aspects"), Names.columns("aspects"));
			Insert.insertStringMap(Example.FORM_COLLECTOR, new File(outDir, Names.file("forms")), Names.table("forms"), Names.columns("forms"));
			Insert.insertStringMap(Example.PERSON_COLLECTOR, new File(outDir, Names.file("persons")), Names.table("persons"), Names.columns("persons"));
			Insert.insertStringMap(Example.TENSE_COLLECTOR, new File(outDir, Names.file("tenses")), Names.table("tenses"), Names.columns("tenses"));
			Insert.insertStringMap(Example.VOICE_COLLECTOR, new File(outDir, Names.file("voices")), Names.table("voices"), Names.columns("voices"));
			Insert.insert(Func.COLLECTOR, new File(outDir, Names.file("funcs")), Names.table("funcs"), Names.columns("funcs"));
			Insert.insert(Theta.COLLECTOR, new File(outDir, Names.file("thetas")), Names.table("thetas"), Names.columns("thetas"));
			Insert.insert(ArgN.SET, ArgN.COMPARATOR, new File(outDir, Names.file("argns")), Names.table("argns"), Names.columns("argns"));

			Insert.insert(RoleSet.COLLECTOR, new File(outDir, Names.file("rolesets")), Names.table("rolesets"), Names.columns("rolesets"));
			Insert.insert(Role.COLLECTOR, new File(outDir, Names.file("roles")), Names.table("roles"), Names.columns("roles"));
			Insert.insert(Example.COLLECTOR, new File(outDir, Names.file("examples")), Names.table("examples"), Names.columns("examples"));
			Insert.insert(Arg.COLLECTOR, new File(outDir, Names.file("args")), Names.table("args"), Names.columns("args"));
			Insert.insert(Rel.COLLECTOR, new File(outDir, Names.file("rels")), Names.table("rels"), Names.columns("rels"));

			Insert.insert(Member.SET, Member.COMPARATOR, new File(outDir, Names.file("members")), Names.table("members"), Names.columns("members"));

			Insert.insert(VnAlias.SET, VnAlias.COMPARATOR, new File(outDir, Names.file("pbrolesets_vnclasses")), Names.table("pbrolesets_vnclasses"), Names.columns("pbrolesets_vnclasses"));
			Insert.insert(FnAlias.SET, FnAlias.COMPARATOR, new File(outDir, Names.file("pbrolesets_fnframes")), Names.table("pbrolesets_fnframes"), Names.columns("pbrolesets_fnframes"));
			Insert.insert(PbRole_VnRole.SET, PbRole_VnRole.COMPARATOR, new File(outDir, Names.file("pbroles_vnroles")), Names.table("pbroles_vnroles"), Names.columns("pbroles_vnroles"));

			Insert.insert(PbWord.COLLECTOR, new File(outDir, Names.file("words")), Names.table("words"), Names.columns("words"));
		}
		Progress.traceTailer("inserts", "done");
	}
}
