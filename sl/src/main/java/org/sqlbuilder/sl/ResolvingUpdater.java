package org.sqlbuilder.sl;

import org.sqlbuilder.common.Progress;
import org.sqlbuilder.common.ProvidesIdTo;
import org.sqlbuilder.common.Update;
import org.sqlbuilder.common.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ResolvingUpdater extends ResolvingInserter
{
	public ResolvingUpdater(final Properties conf) throws IOException, ClassNotFoundException
	{
		super(conf);
	}

	@Override
	public void insert() throws FileNotFoundException
	{
		insertVnAliases();
		insertVnAliases();
	}

	@Override
	protected void insertVnAliases() throws FileNotFoundException
	{
		Progress.tracePending("set", "vnalias");
		final String vnclassidCol = names.column("pbrolesets_vnclasses.vnclassid");
//		Update.update(VnAlias.SET, new File(outDir, names.updateFile("pbrolesets_vnclasses")), names.table("pbrolesets_vnclasses"), //
//				vnClassResolver, //
//				resolved -> vnclassidCol + '=' + Utils.nullable(resolved, Object::toString), //
//				names.column("pbrolesets_vnclasses.vnclass"));
		Progress.traceDone(null);
	}

	@Override
	protected void insertVnRoleAliases() throws FileNotFoundException
	{
		Progress.tracePending("set", "vnaliasrole");
//		final String vnroleidCol = names.column("pbroles_vnroles.roleid");
//		Update.update(VnRoleAlias.SET, new File(outDir, names.updateFile("pbroles_vnroles")), names.table("pbroles_vnroles"), //
//				vnClassRoleResolver, //
//				resolved -> vnroleidCol + '=' + Utils.nullable(resolved, Object::toString), //
//				names.column("pbroles_vnroles.role"));
		Progress.traceDone(null);
	}
}
