package org.sqlbuilder.pb;

import org.sqlbuilder.common.Insert;
import org.sqlbuilder.common.Names;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.annotations.ProvidesIdTo;
import org.sqlbuilder.pb.foreign.FnAlias;
import org.sqlbuilder.pb.foreign.VnAlias;
import org.sqlbuilder.pb.foreign.VnRoleAlias;
import org.sqlbuilder.pb.objects.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Properties;

public class Inserter
{
	protected final Names names;

	protected String header;

	protected File outDir;

	public Inserter(final Properties conf)
	{
		this.names = new Names("pb");
		this.header = conf.getProperty("pb_header");
		this.outDir = new File(conf.getProperty("pb_outdir", "sql/data"));
		if (!this.outDir.exists())
		{
			//noinspection ResultOfMethodCallIgnored
			this.outDir.mkdirs();
		}
	}

	public void insert() throws FileNotFoundException
	{
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

		      @ProvidesIdTo(type = Word.class) var ignored20 = Word.COLLECTOR.open() //
		)
		{
			Progress.tracePending("collector", "aspect");
			Insert.insertStringMap(Example.ASPECT_COLLECTOR, new File(outDir, names.file("aspects")), names.table("aspects"), names.columns("aspects"), header);
			Progress.traceDone();

			Progress.tracePending("collector", "form");
			Insert.insertStringMap(Example.FORM_COLLECTOR, new File(outDir, names.file("forms")), names.table("forms"), names.columns("forms"), header);
			Progress.traceDone();

			Progress.tracePending("collector", "person");
			Insert.insertStringMap(Example.PERSON_COLLECTOR, new File(outDir, names.file("persons")), names.table("persons"), names.columns("persons"), header);
			Progress.traceDone();

			Progress.tracePending("collector", "tense");
			Insert.insertStringMap(Example.TENSE_COLLECTOR, new File(outDir, names.file("tenses")), names.table("tenses"), names.columns("tenses"), header);
			Progress.traceDone();

			Progress.tracePending("collector", "voice");
			Insert.insertStringMap(Example.VOICE_COLLECTOR, new File(outDir, names.file("voices")), names.table("voices"), names.columns("voices"), header);
			Progress.traceDone();

			Progress.tracePending("collector", "func");
			Insert.insert(Func.COLLECTOR, new File(outDir, names.file("funcs")), names.table("funcs"), names.columns("funcs"), header);
			Progress.traceDone();

			Progress.tracePending("collector", "theta");
			Insert.insert(Theta.COLLECTOR, new File(outDir, names.file("thetas")), names.table("thetas"), names.columns("thetas"), header);
			Progress.traceDone();

			Progress.tracePending("set", "argtype");
			Insert.insert(ArgType.SET, ArgType.COMPARATOR, new File(outDir, names.file("argtypes")), names.table("argtypes"), names.columns("argtypes"), header);
			Progress.traceDone();

			Progress.tracePending("collector", "roleset");
			Insert.insert(RoleSet.COLLECTOR, new File(outDir, names.file("rolesets")), names.table("rolesets"), names.columns("rolesets"), header);
			Progress.traceDone();

			Progress.tracePending("collector", "role");
			Insert.insert(Role.COLLECTOR, new File(outDir, names.file("roles")), names.table("roles"), names.columns("roles"), header);
			Progress.traceDone();

			Progress.tracePending("collector", "example");
			Insert.insert(Example.COLLECTOR, new File(outDir, names.file("examples")), names.table("examples"), names.columns("examples"), header);
			Progress.traceDone();

			Progress.tracePending("collector", "arg");
			Insert.insert(Arg.COLLECTOR, new File(outDir, names.file("args")), names.table("args"), names.columns("args"), header);
			Progress.traceDone();

			Progress.tracePending("collector", "rel");
			Insert.insert(Rel.COLLECTOR, new File(outDir, names.file("rels")), names.table("rels"), names.columns("rels"), header);
			Progress.traceDone();

			Progress.tracePending("set", "member");
			Insert.insert(Member.SET, Member.COMPARATOR, new File(outDir, names.file("members")), names.table("members"), names.columns("members"), header);
			Progress.traceDone();

			// R E S O L V A B L E
			insertWords();
			insertFnAliases();
			insertVnAliases();
			insertVnRoleAliases();
		}
	}

	protected void insertWords() throws FileNotFoundException
	{
		Progress.tracePending("collector", "word");
		Insert.insert(Word.COLLECTOR, new File(outDir, names.file("words")), names.table("words"), names.columns("words"), header);
		Progress.traceDone();
	}

	protected void insertFnAliases() throws FileNotFoundException
	{
		Progress.tracePending("collector", "fnalias");
		Insert.insert(FnAlias.SET, FnAlias.COMPARATOR, new File(outDir, names.file("pbrolesets_fnframes")), names.table("pbrolesets_fnframes"), names.columns("pbrolesets_fnframes"), header);
		Progress.traceDone();
	}

	protected void insertVnAliases() throws FileNotFoundException
	{
		Progress.tracePending("set", "vnalias");
		Insert.insert(VnAlias.SET, VnAlias.COMPARATOR, new File(outDir, names.file("pbrolesets_vnclasses")), names.table("pbrolesets_vnclasses"), names.columns("pbrolesets_vnclasses"), header);
		Progress.traceDone();
	}

	protected void insertVnRoleAliases() throws FileNotFoundException
	{
		Progress.tracePending("set", "vnaliasrole");
		Insert.insert(VnRoleAlias.SET, VnRoleAlias.COMPARATOR, new File(outDir, names.file("pbroles_vnroles")), names.table("pbroles_vnroles"), names.columns("pbroles_vnroles"), header);
		Progress.traceDone();
	}
}
