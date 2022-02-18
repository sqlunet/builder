package org.sqlbuilder.sl;

import org.sqlbuilder.common.Insert;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.common.Resolver2;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.sl.foreign.VnAlias;
import org.sqlbuilder.sl.foreign.VnRoleAlias;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class ResolvingInserter extends Inserter
{
	protected final String vnClassSerFile;

	protected final String vnClassRoleSerFile;

	protected final String pbRoleSetSerFile;

	protected final String pbRoleSerFile;

	protected final VnClassResolver vnClassResolver;

	protected final VnClassRoleResolver vnRoleResolver;

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
		this.pbRoleSetResolver = new PbRoleSetResolver(this.pbRoleSetSerFile);

		this.vnRoleResolver = new VnClassRoleResolver(vnClassRoleSerFile);
		this.pbRoleResolver = new PbRoleResolver(this.pbRoleSerFile);
	}

	@Override
	protected void insertVnAliases() throws FileNotFoundException
	{
		Progress.tracePending("set", "vnalias");
		Insert.resolveAndInsert(VnAlias.SET, VnAlias.COMPARATOR, new File(outDir, names.file("pbrolesets_vnclasses")), names.table("pbrolesets_vnclasses"), names.columns("pbrolesets_vnclasses"), //
				new Resolver2<>(pbRoleSetResolver, vnClassResolver), //
				r -> Utils.nullable(r, Objects::toString), //
				names.column("pbrolesets_vnclasses.pbrolesetid"), //
				names.column("pbrolesets_vnclasses.vnclassid"));
		Progress.traceDone(null);
	}

	@Override
	protected void insertVnRoleAliases() throws FileNotFoundException
	{
		Progress.tracePending("set", "vnaliasrole");
		Insert.resolveAndInsert(VnRoleAlias.SET, VnRoleAlias.COMPARATOR, new File(outDir, names.file("pbroles_vnroles")), names.table("pbroles_vnroles"), names.columns("pbroles_vnroles"), //
				new Resolver2<>(pbRoleResolver, vnRoleResolver), //
				r -> r == null ? "NULL,NULL,NULL,NULL" : String.format("%s,%s,%s,%s", r.first.first, r.first.second, r.second.first, r.second.second), //
				names.column("pbroles_vnroles.pbrolesetid"), //
				names.column("pbroles_vnroles.pbroleid"), //
				names.column("pbroles_vnroles.vnclassid"), //
				names.column("pbroles_vnroles.vnroleid"));
		Progress.traceDone(null);
	}
}
