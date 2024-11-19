package org.sqlbuilder.pm;

import org.sqlbuilder.annotations.ProvidesIdTo;
import org.sqlbuilder.common.Insert;
import org.sqlbuilder.common.Names;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.pm.objects.PmPredicate;
import org.sqlbuilder.pm.objects.PmRole;
import org.sqlbuilder2.ser.Pair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PmResolvingProcessor extends PmProcessor
{
	protected final Names names;
	protected final String wordSerFile;
	protected final String vnWordSerFile;
	protected final String pbWordSerFile;
	protected final String fnWordSerFile;
	protected final String sensekeySerFile;
	protected final String vnRoleSerFile;
	protected final String pbRoleSerFile;
	protected final String fnRoleSerFile;
	protected final String fnLexUnitSerFile;
	protected final WordResolver wordResolver;
	protected final VnWordResolver vnWordResolver;
	protected final PbWordResolver pbWordResolver;
	protected final FnWordResolver fnWordResolver;
	protected final SensekeyResolver sensekeyResolver;
	protected final VnRoleResolver vnRoleResolver;
	protected final PbRoleResolver pbRoleResolver;
	protected final FnRoleResolver fnRoleResolver;
	protected final FnLexUnitResolver fnLexUnitResolver;
	protected File outDir;

	public PmResolvingProcessor(final Properties conf) throws IOException, ClassNotFoundException
	{
		super(conf);

		this.names = new Names("pm");

		// header
		this.header += "\n-- " + conf.getProperty("wn_resolve_against");
		this.header += "\n-- " + conf.getProperty("vn_resolve_against");
		this.header += "\n-- " + conf.getProperty("pb_resolve_against");

		// output
		this.outDir = new File(conf.getProperty("pm_outdir_resolved", "sql/data_resolved"));
		if (!this.outDir.exists())
		{
			//noinspection ResultOfMethodCallIgnored
			this.outDir.mkdirs();
		}

		// resolve
		this.wordSerFile = conf.getProperty("word_nids");
		this.sensekeySerFile = conf.getProperty("sense_nids");
		this.vnRoleSerFile = conf.getProperty("vnrole_nids");
		this.pbRoleSerFile = conf.getProperty("pbrole_nids");
		this.fnRoleSerFile = conf.getProperty("fnrole_nids");
		this.fnLexUnitSerFile = conf.getProperty("fnlexunit_nids");
		this.vnWordSerFile = conf.getProperty("vnword_nids");
		this.pbWordSerFile = conf.getProperty("pbword_nids");
		this.fnWordSerFile = conf.getProperty("fnword_nids");

		this.wordResolver = new WordResolver(wordSerFile);
		this.vnWordResolver = new VnWordResolver(vnWordSerFile);
		this.pbWordResolver = new PbWordResolver(pbWordSerFile);
		this.fnWordResolver = new FnWordResolver(fnWordSerFile);
		this.sensekeyResolver = new SensekeyResolver(sensekeySerFile);
		this.vnRoleResolver = new VnRoleResolver(vnRoleSerFile);
		this.pbRoleResolver = new PbRoleResolver(pbRoleSerFile);
		this.fnRoleResolver = new FnRoleResolver(this.fnRoleSerFile);
		this.fnLexUnitResolver = new FnLexUnitResolver(this.fnLexUnitSerFile);
	}

	@Override
	public void run() throws IOException
	{
		var inputFile = new File(pMHome, pMFile);
		process(inputFile, PmRole::parse, null);
		try (@ProvidesIdTo(type = PmPredicate.class) var ignored1 = PmPredicate.COLLECTOR.open())
		{
			Insert.insert(PmPredicate.COLLECTOR, new File(outDir, names.file("predicates")), names.table("predicates"), names.columns("predicates"), header);
			try (@ProvidesIdTo(type = PmRole.class) var ignored2 = PmRole.COLLECTOR.open())
			{
				Insert.insert(PmRole.COLLECTOR, new File(outDir, names.file("roles")), names.table("roles"), names.columns("roles"), header);

				try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("pms"))), true, StandardCharsets.UTF_8))
				{
					ps.println("-- " + header);
					processPmFile(ps, inputFile, names.table("pms"), names.columns("pms", true), (entry, i) -> {

						var wordid = wordResolver.apply(entry.word);
						var sk = sensekeyResolver.apply(entry.sensekey);

						var vnWordid = this.vnWordResolver.apply(entry.word);
						var vn = vnRoleResolver.apply(new Pair<>(entry.vn.clazz, entry.vn.role));
						var pbWordid = this.pbWordResolver.apply(entry.word);
						var pb = pbRoleResolver.apply(new Pair<>(entry.pb.roleset, entry.pb.arg));
						var fnWordid = this.fnWordResolver.apply(entry.word);
						var fn = fnRoleResolver.apply(new Pair<>(entry.fn.frame, entry.fn.fetype));
						var fnLu = fnLexUnitResolver.apply(new Pair<>(entry.fn.frame, entry.fn.lu));

						var wordResolved = Utils.nullableInt(wordid);
						var senseResolved = String.format("%s", sk == null ? "NULL" : Utils.nullableInt(sk.getValue()));

						var vnWordResolved = Utils.nullableInt(vnWordid);
						var pbWordResolved = Utils.nullableInt(pbWordid);
						var fnWordResolved = Utils.nullableInt(fnWordid);
						var vnResolved = String.format("%s", vn == null ? "NULL,NULL" : String.format("%s,%s", Utils.nullableInt(vn.second), Utils.nullableInt(vn.first)));
						var pbResolved = String.format("%s", pb == null ? "NULL,NULL" : String.format("%s,%s", Utils.nullableInt(pb.second), Utils.nullableInt(pb.first)));
						var fnResolved = String.format("%s", fn == null ? "NULL,NULL,NULL" : String.format("%s,%s,%s", Utils.nullableInt(fn.second), Utils.nullableInt(fn.first), Utils.nullableInt(fn.third)));
						var fnLuResolved = String.format("%s", fnLu == null ? "NULL" : String.format("%s", Utils.nullableInt(fnLu.first)));

						insertRow(ps, i, String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", entry.dataRow(), wordResolved, vnWordResolved, pbWordResolved, fnWordResolved, senseResolved, vnResolved, pbResolved, fnResolved, fnLuResolved));
					});
				}
			}
		}
	}
}
