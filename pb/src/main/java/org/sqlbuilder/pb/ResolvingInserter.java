package org.sqlbuilder.pb;

import org.sqlbuilder.common.Insert;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.pb.foreign.FnAlias;
import org.sqlbuilder.pb.foreign.VnAlias;
import org.sqlbuilder.pb.foreign.PbRole_VnRole;
import org.sqlbuilder.pb.objects.Word;
import org.sqlbuilder2.ser.DeSerialize;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class ResolvingInserter extends Inserter
{
	protected final String wordSerFile;

	protected final String vnClassSerFile;

	protected final String vnRoleSerFile;

	protected final String vnClassRoleSerFile;

	protected final String fnFrameSerFile;

	protected final PbWordResolver wordResolver;

	protected final PbVnClassResolver vnClassResolver;

	protected final PbVnRoleResolver vnRoleResolver;

	protected final PbVnClassRoleResolver vnClassRoleResolver;

	protected final PbFnFrameResolver fnFrameResolver;

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
		this.wordSerFile = conf.getProperty("word_nids");
		this.vnClassSerFile = conf.getProperty("vnclass_nids");
		this.vnRoleSerFile = conf.getProperty("vnrole_nids");
		this.vnClassRoleSerFile = conf.getProperty("vnclass_vnrole_nids");
		this.fnFrameSerFile = conf.getProperty("fnframe_nids");

		this.wordResolver = new PbWordResolver(wordSerFile);
		this.vnClassResolver = new PbVnClassResolver(vnClassSerFile);
		this.vnRoleResolver = new PbVnRoleResolver(vnClassSerFile);
		this.vnClassRoleResolver = new PbVnClassRoleResolver(vnClassRoleSerFile);
		this.fnFrameResolver = new PbFnFrameResolver(this.fnFrameSerFile);
	}

	@Override
	protected void insertWords() throws FileNotFoundException
	{
		Progress.tracePending("collector", "word");
		Insert.resolveAndInsert(Word.COLLECTOR, new File(outDir, names.file("words")), names.table("words"), names.columns("words"), true, //
				wordResolver, //
				Objects::toString, //
				names.column("words.wordid"));
		Progress.traceDone(null);
	}

	@Override
	protected void insertFnAliases() throws FileNotFoundException
	{
		Progress.tracePending("set", "fnalias");
		Insert.resolveAndInsert(FnAlias.SET, FnAlias.COMPARATOR, new File(outDir, names.file("pbrolesets_fnframes")), names.table("pbrolesets_fnframes"), names.columns("pbrolesets_fnframes"), //
				fnFrameResolver, //
				e -> Utils.nullable(e, Objects::toString), //
				names.column("pbrolesets_fnframes.fnframeid"));
		Progress.traceDone(null);
	}

	@Override
	protected void insertVnAliases() throws FileNotFoundException
	{
		Progress.tracePending("set", "vnalias");
		Insert.resolveAndInsert(VnAlias.SET, VnAlias.COMPARATOR, new File(outDir, names.file("pbrolesets_vnclasses")), names.table("pbrolesets_vnclasses"), names.columns("pbrolesets_vnclasses"), //
				vnClassResolver, //
				e -> Utils.nullable(e, Objects::toString), //
				names.column("pbrolesets_vnclasses.vnclassid"));
		Progress.traceDone(null);
	}

	@Override
	protected void insertVnAliasRoles() throws FileNotFoundException
	{
		Progress.tracePending("set", "vnaliasrole");
		Insert.resolveAndInsert(PbRole_VnRole.SET, PbRole_VnRole.COMPARATOR, new File(outDir, names.file("pbroles_vnroles")), names.table("pbroles_vnroles"), names.columns("pbroles_vnroles"), //
				vnRoleResolver, //
				e -> Utils.nullable(e, Objects::toString), //
				names.column("pbroles_vnroles.vnclassid"), //
				names.column("pbroles_vnroles.vnroleid"));
		Progress.traceDone(null);
	}
}
