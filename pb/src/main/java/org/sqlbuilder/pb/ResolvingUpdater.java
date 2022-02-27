package org.sqlbuilder.pb;

import org.sqlbuilder.common.Progress;
import org.sqlbuilder.annotations.ProvidesIdTo;
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
		Update.update(Word.COLLECTOR.keySet(), new File(outDir, names.updateFile("words")), names.table("words"), //
				wordResolver, //
				resolved -> wordidCol + '=' + Utils.nullable(resolved, Object::toString), //
				resolving -> String.format("%s='%s'", wordCol, resolving));
		Progress.traceDone();
	}

	@Override
	protected void insertFnAliases() throws FileNotFoundException
	{
		Progress.tracePending("set", "fnalias");
		final String fnframeidCol = names.column("pbrolesets_fnframes.fnframeid");
		final String vnclassCol = names.column("pbrolesets_fnframes.fnframe");
		Update.update(FnAlias.SET, new File(outDir, names.updateFile("pbrolesets_fnframes")), names.table("pbrolesets_fnframes"), //
				fnFrameResolver, //
				resolved -> fnframeidCol + '=' + Utils.nullable(resolved, Object::toString), //
				resolving -> String.format("%s='%s'", vnclassCol, resolving));
		Progress.traceDone();
	}

	@Override
	protected void insertVnAliases() throws FileNotFoundException
	{
		Progress.tracePending("set", "vnalias");
		final String vnclassidCol = names.column("pbrolesets_vnclasses.vnclassid");
		final String vnclassCol = names.column("pbrolesets_vnclasses.vnclass");
		Update.update(VnAlias.SET, new File(outDir, names.updateFile("pbrolesets_vnclasses")), names.table("pbrolesets_vnclasses"), //
				vnClassResolver, //
				resolved -> vnclassidCol + '=' + Utils.nullable(resolved, Object::toString), //
				resolving -> String.format("%s='%s'", vnclassCol, resolving));
		Progress.traceDone();
	}

	@Override
	protected void insertVnRoleAliases() throws FileNotFoundException
	{
		Progress.tracePending("set", "vnaliasrole");
		final String vnroleidCol = names.column("pbroles_vnroles.roleid");
		final String vnroleCol = names.column("pbroles_vnroles.vntheta");
		Update.update(VnRoleAlias.SET, new File(outDir, names.updateFile("pbroles_vnroles")), names.table("pbroles_vnroles"), //
				vnClassRoleResolver, //
				resolved -> vnroleidCol + '=' + Utils.nullable(resolved, Object::toString), //
				resolving -> String.format("%s='%s'", vnroleCol, resolving));
		Progress.traceDone();
	}
}
