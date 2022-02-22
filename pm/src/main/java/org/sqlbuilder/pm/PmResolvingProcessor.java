package org.sqlbuilder.pm;

import org.sqlbuilder.common.Names;
import org.sqlbuilder.common.Utils;
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

	protected File outDir;

	protected final String wordSerFile;

	protected final String sensekeySerFile;

	protected final String vnRoleSerFile;

	protected final String pbRoleSerFile;

	protected final String fnRoleSerFile;

	protected final WordResolver wordResolver;

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

		this.wordResolver = new WordResolver(wordSerFile);
		this.sensekeyResolver = new SensekeyResolver(sensekeySerFile);
		this.vnRoleResolver = new VnClassRoleResolver(vnRoleSerFile);
		this.pbRoleResolver = new PbRoleResolver(pbRoleSerFile);
		this.fnRoleResolver = new FnRoleResolver(this.fnRoleSerFile);
	}

	@Override
	public void run() throws IOException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("pms"))), true, StandardCharsets.UTF_8))
		{
			processPmFile(ps, new File(pMHome, pMFile), names.table("pms"), names.columns("pms", true), (entry, i) -> {

				var wordid = wordResolver.apply(entry.word);
				var sk = sensekeyResolver.apply(entry.sensekey);
				var vn = vnRoleResolver.apply(new Pair<>(entry.vn.clazz, entry.vn.theta));
				var pb = pbRoleResolver.apply(new Pair<>(entry.pb.roleset, entry.pb.arg));
				var fn = fnRoleResolver.apply(new Pair<>(entry.fn.frame, entry.fn.fetype));

				var wordResolved = Utils.nullableInt(wordid);
				var senseResolved = String.format("%s", sk == null ? "NULL" : Utils.nullableInt(sk.getValue()));
				var vnResolved = String.format("%s", vn == null ? "NULL,NULL" : String.format("%s,%s", Utils.nullableInt(vn.first), Utils.nullableInt(vn.second)));
				var pbResolved = String.format("%s", pb == null ? "NULL,NULL" : String.format("%s,%s", Utils.nullableInt(pb.first), Utils.nullableInt(pb.second)));
				var fnResolved = String.format("%s", fn == null ? "NULL,NULL,NULL" : String.format("%s,%s,%s", Utils.nullableInt(fn.first), Utils.nullableInt(fn.second), Utils.nullableInt(fn.third)));
				insertRow(ps, i, String.format("%s,%s,%s,%s,%s,%s", entry.dataRow(), wordResolved, senseResolved, vnResolved, pbResolved, fnResolved));
			});
		}
	}
}
