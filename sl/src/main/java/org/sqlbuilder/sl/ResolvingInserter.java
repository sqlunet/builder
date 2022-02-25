package org.sqlbuilder.sl;

import org.sqlbuilder.common.Insert;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.common.Resolver2;
import org.sqlbuilder.sl.foreign.VnAlias;
import org.sqlbuilder.sl.foreign.VnRoleAlias;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ResolvingInserter extends Inserter
{
	protected final String vnClassSerFile;

	protected final String vnRoleSerFile;

	protected final String pbRoleSetSerFile;

	protected final String pbRoleSerFile;

	protected final VnClassResolver vnClassResolver;

	protected final VnRoleResolver vnRoleResolver;

	protected final PbRoleSetResolver pbRoleSetResolver;

	protected final PbRoleResolver pbRoleResolver;

	public ResolvingInserter(final Properties conf) throws IOException, ClassNotFoundException
	{
		super(conf);

		// output
		this.outDir = new File(conf.getProperty("sl_outdir_resolved", "sql/data_resolved"));
		if (!this.outDir.exists())
		{
			this.outDir.mkdirs();
		}

		// resolve
		this.resolve = true;
		this.pbRoleSetSerFile = conf.getProperty("pbroleset_nids");
		this.pbRoleSerFile = conf.getProperty("pbrole_nids");
		this.vnClassSerFile = conf.getProperty("vnclass_nids");
		this.vnRoleSerFile = conf.getProperty("vnrole_nids");

		this.pbRoleSetResolver = new PbRoleSetResolver(this.pbRoleSetSerFile);
		this.pbRoleResolver = new PbRoleResolver(this.pbRoleSerFile);
		this.vnClassResolver = new VnClassResolver(vnClassSerFile);
		this.vnRoleResolver = new VnRoleResolver(vnRoleSerFile);
	}

	@Override
	protected void insertVnAliases() throws FileNotFoundException
	{
		Progress.tracePending("set", "vnalias");
		Insert.resolveAndInsert(VnAlias.SET, VnAlias.COMPARATOR, new File(outDir, names.file("pbrolesets_vnclasses")), names.table("pbrolesets_vnclasses"), names.columns("pbrolesets_vnclasses"), //
				new Resolver2<>(pbRoleSetResolver, vnClassResolver), //
				VnAlias.RESOLVE_RESULT_STRINGIFIER, //
				names.column("pbrolesets_vnclasses.pbrolesetid"), //
				names.column("pbrolesets_vnclasses.vnclassid"));
		Progress.traceDone();
	}

	@Override
	protected void insertVnRoleAliases() throws FileNotFoundException
	{
		Progress.tracePending("set", "vnaliasrole");
		Insert.resolveAndInsert(VnRoleAlias.SET, VnRoleAlias.COMPARATOR, new File(outDir, names.file("pbroles_vnroles")), names.table("pbroles_vnroles"), names.columns("pbroles_vnroles"), //
				new Resolver2<>(pbRoleResolver, vnRoleResolver), //
				VnRoleAlias.RESOLVE_RESULT_STRINGIFIER, //
				names.column("pbroles_vnroles.pbrolesetid"), //
				names.column("pbroles_vnroles.pbroleid"), //
				names.column("pbroles_vnroles.vnclassid"), //
				names.column("pbroles_vnroles.vnroleid"), //;
				names.column("pbroles_vnroles.vnroletypeid"));
		Progress.traceDone();
	}
}
