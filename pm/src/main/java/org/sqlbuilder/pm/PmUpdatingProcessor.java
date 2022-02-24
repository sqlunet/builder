package org.sqlbuilder.pm;

import org.sqlbuilder.common.Utils;
import org.sqlbuilder.pm.objects.PmEntry;
import org.sqlbuilder2.ser.Pair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.function.BiConsumer;

public class PmUpdatingProcessor extends PmResolvingProcessor
{
	public PmUpdatingProcessor(final Properties conf) throws IOException, ClassNotFoundException
	{
		super(conf);

		// output
		this.outDir = new File(conf.getProperty("pm_outdir_updated", "sql/data_updated"));
		if (!this.outDir.exists())
		{
			this.outDir.mkdirs();
		}
	}

	@Override
	public void run() throws IOException
	{
		var inputFile = new File(pMHome, pMFile);
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.updateFile("pms.wn"))), true, StandardCharsets.UTF_8))
		{
			processPmFile(ps, inputFile, (entry, i) -> updateWordSenseRow(ps, names.table("pms"), i, entry, //
					names.column("pms.wordid"), //
					names.column("pms.synsetid"), //
					names.column("pms.word"), //
					names.column("pms.sensekey")));
		}

		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.updateFile("pms.xn"))), true, StandardCharsets.UTF_8))
		{
			processPmFile(ps, inputFile, (entry, i) -> {

				updateVnPbFnRow(ps, names.table("pms"), i, entry, //
						names.column("pms.vnwordid"), //
						names.column("pms.pbwordid"), //
						names.column("pms.fnwordid"), //
						names.column("pms.vnclassid"), //
						names.column("pms.vnroleid"), //
						names.column("pms.pbrolesetid"), //
						names.column("pms.pbroleid"), //
						names.column("pms.fnframeid"), //
						names.column("pms.fnfeid"), //
						names.column("pms.fnluid"), //
						names.column("pms.word"), //
						names.column("pms.vnclass"), //
						names.column("pms.vnrole"), //
						names.column("pms.pbroleset"), //
						names.column("pms.pbrole"), //
						names.column("pms.fnframe"), //
						names.column("pms.fnfe") //
				);
			});
		}
	}

	private void updateWordSenseRow(final PrintStream ps, final String table, final int index, final PmEntry entry, final String... columns)
	{
		var wordid = wordResolver.apply(entry.word);
		var wordResolved = Utils.nullableInt(wordid);
		var sk = sensekeyResolver.apply(entry.sensekey);
		var senseResolved = sk == null ? "NULL" : Utils.nullableInt(sk.getValue());

		var setClause = String.format("%s=%s,%s=%s ", //
				columns[0], wordResolved, //
				columns[1], senseResolved);
		var whereClause = String.format("%s=%s,%s=%s", //
				columns[2], Utils.quote(Utils.escape(entry.word)), //
				columns[3], entry.sensekey == null ? "NULL" : Utils.quote(Utils.escape(Utils.escape(entry.sensekey))) //
		);
		ps.printf("UPDATE %s SET %s WHERE %s; -- %d%n", table, setClause, whereClause, index + 1);
	}

	private void updateVnPbFnRow(final PrintStream ps, final String table, final int index, final PmEntry entry, final String... columns)
	{
		var vnWordid = this.vnWordResolver.apply(entry.word);
		var pbWordid = this.pbWordResolver.apply(entry.word);
		var fnWordid = this.fnWordResolver.apply(entry.word);
		var vn = vnRoleResolver.apply(new Pair<>(entry.vn.clazz, entry.vn.theta));
		var pb = pbRoleResolver.apply(new Pair<>(entry.pb.roleset, entry.pb.arg));
		var fn = fnRoleResolver.apply(new Pair<>(entry.fn.frame, entry.fn.fetype));

		var setClause = String.format("%s=%s,%s=%s,%s=%s,%s=%s,%s=%s,%s=%s,%s=%s,%s=%s,%s=%s,%s=%s", columns[0], Utils.nullableInt(vnWordid), //
				columns[1], Utils.nullableInt(pbWordid), //
				columns[2], Utils.nullableInt(fnWordid), //
				columns[3], vn == null ? "NULL" : Utils.nullableInt(vn.first), //
				columns[4], vn == null ? "NULL" : Utils.nullableInt(vn.second), //
				columns[5], pb == null ? "NULL" : Utils.nullableInt(pb.first), //
				columns[6], pb == null ? "NULL" : Utils.nullableInt(pb.second), //
				columns[7], fn == null ? "NULL" : Utils.nullableInt(fn.first), //
				columns[8], fn == null ? "NULL" : Utils.nullableInt(fn.second), //
				columns[9], fn == null ? "NULL" : Utils.nullableInt(fn.third) //
		);
		var whereClause = String.format("%s=%s,%s=%s,%s=%s,%s=%s,%s=%s,%s=%s,%s=%s", //
				columns[10], entry.word == null ? "NULL" : Utils.quote(Utils.escape(entry.word)), //
				columns[11], entry.vn.clazz == null ? "NULL" : Utils.quote(Utils.escape(entry.vn.clazz)), //
				columns[12], entry.vn.theta == null ? "NULL" : Utils.quote(Utils.escape(entry.vn.theta)), //
				columns[13], entry.pb.roleset == null ? "NULL" : Utils.quote(Utils.escape(entry.pb.roleset)), //
				columns[14], entry.pb.arg == null ? "NULL" : Utils.quote(Utils.escape(entry.pb.arg)), //
				columns[15], entry.fn.frame == null ? "NULL" : Utils.quote(Utils.escape(entry.fn.frame)), //
				columns[16], entry.fn.fetype == null ? "NULL" : Utils.quote(Utils.escape(entry.fn.fetype)) //
		);
		ps.printf("UPDATE %s SET %s WHERE %s; -- %d%n", table, setClause, whereClause, index + 1);
	}

	protected void processPmFile(final PrintStream ps, final File file, final BiConsumer<PmEntry, Integer> consumer) throws IOException
	{
		process(file, PmEntry::parse, consumer);
	}
}
