package org.sqlbuilder.sl;

import org.sqlbuilder.common.Progress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ResolvingInserter extends Inserter
{
	protected final String vnClassSerFile;

	protected final String vnClassRoleSerFile;

	protected final String pbRoleSetSerFile;

	protected final String pbRoleSerFile;

	protected final VnClassResolver vnClassResolver;

	protected final VnClassRoleResolver vnClassRoleResolver;

	protected final PbRoleSetResolver pbRoleSetResolver;

	protected final PbRoleResolver pbRoleResolver;

	public ResolvingInserter(final Properties conf) throws IOException, ClassNotFoundException
	{
		super(conf);

		// output
		this.outDir = new File(conf.getProperty("vn_outdir_resolved", "sql/data_resolved"));
		if (!this.outDir.exists())
		{
			this.outDir.mkdirs();
		}

		// resolve
		this.resolve = true;
		this.vnClassSerFile = conf.getProperty("vnclass_nids");
		this.vnClassRoleSerFile = conf.getProperty("vnclass_vnrole_nids");
		this.pbRoleSetSerFile = conf.getProperty("pbroleset_nids");
		this.pbRoleSerFile = conf.getProperty("pbroleset_pbrole_nids");

		this.vnClassResolver = new VnClassResolver(vnClassSerFile);
		this.vnClassRoleResolver = new VnClassRoleResolver(vnClassRoleSerFile);
		this.pbRoleSetResolver = new PbRoleSetResolver(this.pbRoleSetSerFile);
		this.pbRoleResolver = new PbRoleResolver(this.pbRoleSerFile);
	}

	@Override
	protected void insertVnAliases() throws FileNotFoundException
	{
		Progress.tracePending("set", "vnalias");
//		Insert.resolveAndInsert(VnAlias.SET, VnAlias.COMPARATOR, new File(outDir, names.file("pbrolesets_vnclasses")), names.table("pbrolesets_vnclasses"), names.columns("pbrolesets_vnclasses"), //
//				vnClassResolver, //
//				r -> Utils.nullable(r, Objects::toString), //
//				names.column("pbrolesets_vnclasses.vnclassid"));
		Progress.traceDone(null);
	}

	@Override
	protected void insertVnRoleAliases() throws FileNotFoundException
	{
		Progress.tracePending("set", "vnaliasrole");
//		Insert.resolveAndInsert(VnRoleAlias.SET, VnRoleAlias.COMPARATOR, new File(outDir, names.file("pbroles_vnroles")), names.table("pbroles_vnroles"), names.columns("pbroles_vnroles"), //
//				vnClassRoleResolver, //
//				r -> r==null ? "NULL,NULL,NULL" : String.format("%s,%s,%s", r.first, r.second, r.third), //
//				names.column("pbroles_vnroles.vnclassid"), //
//				names.column("pbroles_vnroles.vnroleid"), //
//				names.column("pbroles_vnroles.vnroletypeid"));
		Progress.traceDone(null);
	}
}
