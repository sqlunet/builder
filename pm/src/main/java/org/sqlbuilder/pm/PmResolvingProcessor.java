package org.sqlbuilder.pm;

import org.sqlbuilder.common.Names;
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

	protected final String fnFrameSerFile;

	protected final WordResolver wordResolver;

	protected final SensekeyResolver sensekeyResolver;

	protected final VnClassRoleResolver vnRoleResolver;

	protected final PbRoleResolver pbRoleResolver;

	protected final FnFrameResolver fnFrameResolver;

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
		this.sensekeySerFile = conf.getProperty("sensekey_nids");
		this.vnRoleSerFile = conf.getProperty("vnclass_vnrole_nids");
		this.pbRoleSerFile = conf.getProperty("pbroleset_pbrole_nids");
		this.fnFrameSerFile = conf.getProperty("fnframe_nids");

		this.wordResolver = new WordResolver(wordSerFile);
		this.sensekeyResolver = new SensekeyResolver(sensekeySerFile);
		this.vnRoleResolver = new VnClassRoleResolver(vnRoleSerFile);
		this.pbRoleResolver = new PbRoleResolver(pbRoleSerFile);
		this.fnFrameResolver = new FnFrameResolver(this.fnFrameSerFile);
	}

	@Override
	public void run() throws IOException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("pms"))), true, StandardCharsets.UTF_8))
		{
			processPmFile(ps, new File(pMHome, pMFile), names.table("pms"), names.columns("pms", true), (row, i) -> {

				var wordid = wordResolver.apply(row.word);
				var sk = sensekeyResolver.apply(row.word);
				var vn = vnRoleResolver.apply(new Pair<>(row.vn.clazz, row.vn.theta));
				var pb = pbRoleResolver.apply(new Pair<>(row.pb.roleset, row.pb.arg));
				var fn = fnFrameResolver.apply(row.fn.frame);

				var resolved = String.format("", wordid, sk.getValue(), vn.first, vn.second, pb.first, pb.second, fn);
				insertRow(ps, i, row.dataRow() + ',' + resolved);
			});
		}
	}
}
