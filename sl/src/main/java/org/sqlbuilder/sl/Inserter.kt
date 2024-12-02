package org.sqlbuilder.sl;

import org.sqlbuilder.common.Insert;
import org.sqlbuilder.common.Names;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.sl.foreign.VnAlias;
import org.sqlbuilder.sl.foreign.VnRoleAlias;

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
		this.names = new Names("sl");
		this.header = conf.getProperty("sl_header");
		this.outDir = new File(conf.getProperty("sl_outdir", "sql/data"));
		if (!this.outDir.exists())
		{
			//noinspection ResultOfMethodCallIgnored
			this.outDir.mkdirs();
		}
	}

	public void insert() throws FileNotFoundException
	{
		// R E S O L V A B L E
		insertVnAliases();
		insertVnRoleAliases();
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
