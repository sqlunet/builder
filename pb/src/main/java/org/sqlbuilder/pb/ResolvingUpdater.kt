package org.sqlbuilder.pb;

import org.sqlbuilder.annotations.ProvidesIdTo;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.common.Update;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.pb.foreign.FnAlias;
import org.sqlbuilder.pb.foreign.VnAlias;
import org.sqlbuilder.pb.foreign.VnRoleAlias;
import org.sqlbuilder.pb.objects.Word;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ResolvingUpdater extends ResolvingInserter
{
	public ResolvingUpdater(final Properties conf) throws IOException, ClassNotFoundException
	{
		super(conf);

		// output
		this.outDir = new File(conf.getProperty("pm_outdir_updated", "sql/data_updated"));
		if (!this.outDir.exists())
		{
			//noinspection ResultOfMethodCallIgnored
			this.outDir.mkdirs();
		}
	}

	@Override
	public void insert() throws FileNotFoundException
	{
		try (@ProvidesIdTo(type = Word.class) var ignored30 = Word.COLLECTOR.open())
		{
			insertWords();
			insertFnAliases();
			insertVnAliases();
			insertVnRoleAliases();
		}
	}

	@Override
	protected void insertWords() throws FileNotFoundException
	{
		Progress.tracePending("collector", "word");
		final String wordidCol = names.column("words.wordid");
		final String wordCol = names.column("words.word");
		Update.update(Word.COLLECTOR.keySet(), new File(outDir, names.updateFile("words")), header, names.table("words"), //
				wordResolver, //
				resolved -> String.format("%s=%s", wordidCol, Utils.nullableInt(resolved)), //
				resolving -> String.format("%s='%s'", wordCol, Utils.escape(resolving)));
		Progress.traceDone();
	}

	@Override
	protected void insertFnAliases() throws FileNotFoundException
	{
		Progress.tracePending("set", "fnalias");
		final String fnframeidCol = names.column("pbrolesets_fnframes.fnframeid");
		final String vnclassCol = names.column("pbrolesets_fnframes.fnframe");
		Update.update(FnAlias.SET, new File(outDir, names.updateFile("pbrolesets_fnframes")), header, names.table("pbrolesets_fnframes"), //
				fnFrameResolver, //
				resolved -> String.format("%s=%s", fnframeidCol, Utils.nullableInt(resolved)), //
				resolving -> String.format("%s='%s'", vnclassCol, Utils.escape(resolving)));
		Progress.traceDone();
	}

	@Override
	protected void insertVnAliases() throws FileNotFoundException
	{
		Progress.tracePending("set", "vnalias");
		final String vnclassidCol = names.column("pbrolesets_vnclasses.vnclassid");
		final String vnclassCol = names.column("pbrolesets_vnclasses.vnclass");
		Update.update(VnAlias.SET, new File(outDir, names.updateFile("pbrolesets_vnclasses")), header, names.table("pbrolesets_vnclasses"), //
				vnClassResolver, //
				resolved -> String.format("%s=%s", vnclassidCol, Utils.nullableInt(resolved)), //
				resolving -> String.format("%s='%s'", vnclassCol, Utils.escape(resolving)));
		Progress.traceDone();
	}

	@Override
	protected void insertVnRoleAliases() throws FileNotFoundException
	{
		Progress.tracePending("set", "vnaliasrole");
		final String vnclassidCol = names.column("pbroles_vnroles.vnclassid");
		final String vnroleidCol = names.column("pbroles_vnroles.vnroleid");
		final String vnroletypeidCol = names.column("pbroles_vnroles.vnroletypeid");
		final String vnclassCol = names.column("pbroles_vnroles.vnclass");
		final String vnroleCol = names.column("pbroles_vnroles.vntheta");
		Update.update(VnRoleAlias.SET, new File(outDir, names.updateFile("pbroles_vnroles")), header, names.table("pbroles_vnroles"), //
				vnClassRoleResolver, //
				resolved -> resolved == null ?
						String.format("%s=NULL,%s=NULL,%s=NULL", vnclassidCol, vnroleidCol, vnroletypeidCol) :
						String.format("%s=%s,%s=%s,%s=%s", vnclassidCol, Utils.nullableInt(resolved.first), vnroleidCol, Utils.nullableInt(resolved.second), vnroletypeidCol, Utils.nullableInt(resolved.third)), //
				resolving -> String.format("%s='%s' AND %s='%s'", vnclassCol, Utils.escape(resolving.first), vnroleCol, Utils.escape(resolving.second)));
		Progress.traceDone();
	}
}
