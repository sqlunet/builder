package org.sqlbuilder.sl;

import org.sqlbuilder.common.CombinedResolver;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.common.Update;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.sl.foreign.VnClassAlias;
import org.sqlbuilder.sl.foreign.VnRoleAlias;
import org.sqlbuilder2.ser.Pair;
import org.sqlbuilder2.ser.Triplet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.function.Function;

public class ResolvingUpdater extends ResolvingInserter
{
	public ResolvingUpdater(final Properties conf) throws IOException, ClassNotFoundException
	{
		super(conf);

		// output
		this.outDir = new File(conf.getProperty("sl_outdir_updated", "sql/data_updated"));
		if (!this.outDir.exists())
		{
			//noinspection ResultOfMethodCallIgnored
			this.outDir.mkdirs();
		}
	}

	@Override
	public void insert() throws FileNotFoundException
	{
		insertVnAliases();
		insertVnRoleAliases();
	}

	@Override
	protected void insertVnAliases() throws FileNotFoundException
	{
		Progress.tracePending("set", "vnalias");
		final String pbrolesetCol = names.column("pbrolesets_vnclasses.pbroleset");
		final String vnclassCol = names.column("pbrolesets_vnclasses.vnclass");
		final String pbrolesetidCol = names.column("pbrolesets_vnclasses.pbrolesetid");
		final String vnclassidCol = names.column("pbrolesets_vnclasses.vnclassid");
		Update.update(VnClassAlias.SET,
				new File(outDir, names.updateFile("pbrolesets_vnclasses")),
				header,
				names.table("pbrolesets_vnclasses"), 
				new CombinedResolver<>(pbRoleSetResolver, vnClassResolver), 
				resolved -> resolved == null ? String.format("%s=NULL,%s=NULL", pbrolesetidCol, vnclassidCol) : String.format("%s=%s,%s=%s", pbrolesetidCol, Utils.nullableInt(resolved.first), vnclassidCol, Utils.nullableInt(resolved.second)), 
				resolving -> String.format("%s='%s' AND %s='%s'", pbrolesetCol, Utils.escape(resolving.first), vnclassCol, Utils.escape(resolving.second)));
		Progress.traceDone();
	}

	@Override
	protected void insertVnRoleAliases() throws FileNotFoundException
	{
		Progress.tracePending("set", "vnaliasrole");
		final String pbrolesetCol = names.column("pbroles_vnroles.pbroleset");
		final String pbroleCol = names.column("pbroles_vnroles.pbarg");
		final String vnclassCol = names.column("pbroles_vnroles.vnclass");
		final String vnroleCol = names.column("pbroles_vnroles.vntheta");
		final String pbrolesetidCol = names.column("pbroles_vnroles.pbrolesetid");
		final String pbroleidCol = names.column("pbroles_vnroles.pbroleid");
		final String vnclassidCol = names.column("pbroles_vnroles.vnclassid");
		final String vnroleidCol = names.column("pbroles_vnroles.vnroleid");
		final String vnroletypeidCol = names.column("pbroles_vnroles.vnroletypeid");

		final Function<Pair<Pair<Integer, Integer>, Triplet<Integer, Integer, Integer>>, String> setStringifier = r -> 
				r == null ? 
						String.format("%s=NULL,%s=NULL,%s=NULL,%s=NULL,%s=NULL", pbrolesetidCol, pbroleidCol, vnclassidCol, vnroleidCol, vnroletypeidCol) : 
						String.format("%s,%s", 
								r.first == null ? 
										String.format("%s=NULL,%s=NULL", pbrolesetidCol, pbroleidCol) : 
										String.format("%s=%s,%s=%s", pbrolesetidCol, Utils.nullableInt(r.first.first), pbroleidCol, Utils.nullableInt(r.first.second)), 
								r.second == null ?
										
										String.format("%s=NULL,%s=NULL,%s=NULL", vnclassidCol, vnroleidCol, vnroletypeidCol) :
										String.format("%s=%s,%s=%s,%s=%s", vnclassidCol, Utils.nullableInt(r.second.first), vnroleidCol, Utils.nullableInt(r.second.second), vnroletypeidCol, Utils.nullableInt(r.second.third)));

		final Function<Pair<Pair<String, String>, Pair<String, String>>, String> whereStringifier = r -> 
				String.format("%s='%s' AND %s='%s' AND %s='%s' AND %s='%s'", pbrolesetCol, Utils.escape(r.first.first), pbroleCol, Utils.escape(r.first.second), vnclassCol, Utils.escape(r.second.first), vnroleCol, Utils.escape(r.second.second));

		Update.update(VnRoleAlias.SET,
				new File(outDir, names.updateFile("pbroles_vnroles")),
				header,
				names.table("pbroles_vnroles"), 
				new CombinedResolver<>(pbRoleResolver, vnRoleResolver), 
				setStringifier, 
				whereStringifier);
		Progress.traceDone();
	}
}
