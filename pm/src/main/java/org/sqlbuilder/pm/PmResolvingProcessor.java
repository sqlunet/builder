package org.sqlbuilder.pm;

import org.sqlbuilder.common.Insert;
import org.sqlbuilder.common.Names;
import org.sqlbuilder.common.ProvidesIdTo;
import org.sqlbuilder.common.Utils;
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

	protected final File outDir;

	protected final String wordSerFile;

	protected final String vnWordSerFile;

	protected final String pbWordSerFile;

	protected final String fnWordSerFile;

	protected final String sensekeySerFile;

	protected final String vnRoleSerFile;

	protected final String pbRoleSerFile;

	protected final String fnRoleSerFile;

	protected final WordResolver wordResolver;

	protected final VnWordResolver vnWordResolver;

	protected final PbWordResolver pbWordResolver;

	protected final FnWordResolver fnWordResolver;

	protected final SensekeyResolver sensekeyResolver;

	protected final VnClassRoleResolver vnRoleResolver;

	protected final PbRoleResolver pbRoleResolver;

	protected final FnRoleResolver fnRoleResolver;

	public PmResolvingProcessor(final Properties conf) throws IOException, ClassNotFoundException
	{
		super(conf);

		this.names = new Names("pm");

		// output
		this.outDir = new File(conf.getProperty("pm_outdir_resolved", "sql/data_resolved"));
		if (!this.outDir.exists())
		{
			this.outDir.mkdirs();
		}

		// resolve
		this.wordSerFile = conf.getProperty("word_nids");
		this.sensekeySerFile = conf.getProperty("sense_nids");
		this.vnRoleSerFile = conf.getProperty("vnrole_nids");
		this.pbRoleSerFile = conf.getProperty("pbrole_nids");
		this.fnRoleSerFile = conf.getProperty("fnrole_nids");
		this.vnWordSerFile = conf.getProperty("vnword_nids");
		this.pbWordSerFile = conf.getProperty("pbword_nids");
		this.fnWordSerFile = conf.getProperty("fnword_nids");

		this.wordResolver = new WordResolver(wordSerFile);
		this.vnWordResolver = new VnWordResolver(vnWordSerFile);
		this.pbWordResolver = new PbWordResolver(pbWordSerFile);
		this.fnWordResolver = new FnWordResolver(fnWordSerFile);
		this.sensekeyResolver = new SensekeyResolver(sensekeySerFile);
		this.vnRoleResolver = new VnClassRoleResolver(vnRoleSerFile);
		this.pbRoleResolver = new PbRoleResolver(pbRoleSerFile);
		this.fnRoleResolver = new FnRoleResolver(this.fnRoleSerFile);
	}

	@Override
	public void run() throws IOException
	{
		var inputFile = new File(pMHome, pMFile);
		process(inputFile, PmRole::parse, null);
		try (@ProvidesIdTo(type = PmRole.class) var ignored = PmRole.COLLECTOR.open())
		{
			Insert.insert(PmRole.COLLECTOR, new File(outDir, names.file("pmroles")), names.table("pmroles"), names.columns("pmroles"));

			try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("pms"))), true, StandardCharsets.UTF_8))
			{
				processPmFile(ps, inputFile, names.table("pms"), names.columns("pms", true), (entry, i) -> {

					var wordid = wordResolver.apply(entry.word);
					var vnWordid = this.vnWordResolver.apply(entry.word);
					var pbWordid = this.pbWordResolver.apply(entry.word);
					var fnWordid = this.fnWordResolver.apply(entry.word);
					var sk = sensekeyResolver.apply(entry.sensekey);
					var vn = vnRoleResolver.apply(new Pair<>(entry.vn.clazz, entry.vn.theta));
					var pb = pbRoleResolver.apply(new Pair<>(entry.pb.roleset, entry.pb.arg));
					var fn = fnRoleResolver.apply(new Pair<>(entry.fn.frame, entry.fn.fetype));

					var wordResolved = Utils.nullableInt(wordid);
					var vnWordResolved = Utils.nullableInt(vnWordid);
					var pbWordResolved = Utils.nullableInt(pbWordid);
					var fnWordResolved = Utils.nullableInt(fnWordid);
					var senseResolved = String.format("%s", sk == null ? "NULL" : Utils.nullableInt(sk.getValue()));
					var vnResolved = String.format("%s", vn == null ? "NULL,NULL" : String.format("%s,%s", Utils.nullableInt(vn.first), Utils.nullableInt(vn.second)));
					var pbResolved = String.format("%s", pb == null ? "NULL,NULL" : String.format("%s,%s", Utils.nullableInt(pb.first), Utils.nullableInt(pb.second)));
					var fnResolved = String.format("%s", fn == null ? "NULL,NULL,NULL" : String.format("%s,%s,%s", Utils.nullableInt(fn.first), Utils.nullableInt(fn.second), Utils.nullableInt(fn.third)));
					insertRow(ps, i, String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s", entry.dataRow(), wordResolved, vnWordResolved, pbWordResolved, fnWordResolved, senseResolved, vnResolved, pbResolved, fnResolved));
				});
			}
		}
	}
}
