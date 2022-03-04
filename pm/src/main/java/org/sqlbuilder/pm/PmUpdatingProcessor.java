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
			//noinspection ResultOfMethodCallIgnored
			this.outDir.mkdirs();
		}
	}

	@Override
	public void run() throws IOException
	{
		var inputFile = new File(pMHome, pMFile);
		var singleOutput = names.updateFile("pms");
		var wnOutput = names.updateFileNullable("pms.wn");
		var xnOutput = names.updateFileNullable("pms.xn");

		if (wnOutput == null || xnOutput == null)
		{
			try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, singleOutput)), true, StandardCharsets.UTF_8))
			{
				ps.println("-- " + header);
				processPmFile(inputFile, makeWnConsumer(ps));
				processPmFile(inputFile, makeXnConsumer(ps));
			}
		}
		else
		{
			try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, wnOutput)), true, StandardCharsets.UTF_8))
			{
				ps.println("-- " + header);
				processPmFile(inputFile, makeWnConsumer(ps));
			}

			try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, xnOutput)), true, StandardCharsets.UTF_8))
			{
				ps.println("-- " + header);
				processPmFile(inputFile, makeXnConsumer(ps));
			}
		}
	}


	private void updateWordSenseRow(final PrintStream ps, final String table, final int index, final PmEntry entry, final String... columns)
	{
		var wordid = wordResolver.apply(entry.word);
		var wordResolved = Utils.nullableInt(wordid);
		var sk = sensekeyResolver.apply(entry.sensekey);
		var senseResolved = Utils.nullable(sk, x -> Utils.nullableInt(x.getValue()));

		var setClause = String.format("%s=%s,%s=%s ", //
				columns[0], wordResolved, //
				columns[1], senseResolved);
		var whereClause = String.format("%s=%s AND %s=%s", //
				columns[2], Utils.quote(Utils.escape(entry.word)), //
				columns[3], Utils.nullable(entry.sensekey, x -> Utils.quote(Utils.escape(Utils.escape(x)))) //
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
				columns[3], Utils.nullable(vn, x -> Utils.nullableInt(x.first)), //
				columns[4], Utils.nullable(vn, x -> Utils.nullableInt(x.second)), //
				columns[5], Utils.nullable(pb, x -> Utils.nullableInt(x.first)), //
				columns[6], Utils.nullable(pb, x -> Utils.nullableInt(x.second)), //
				columns[7], Utils.nullable(fn, x -> Utils.nullableInt(x.first)), //
				columns[8], Utils.nullable(fn, x -> Utils.nullableInt(x.second)), //
				columns[9], Utils.nullable(fn, x -> Utils.nullableInt(x.third)) //
		);
		var whereClause = String.format("%s=%s AND %s=%s AND %s=%s AND %s=%s AND %s=%s AND %s=%s AND %s=%s", //
				columns[10], Utils.nullable(entry.word, x -> Utils.quote(Utils.escape(x))), //
				columns[11], Utils.nullable(entry.vn.clazz, x -> Utils.quote(Utils.escape(x))), //
				columns[12], Utils.nullable(entry.vn.theta, x -> Utils.quote(Utils.escape(x))), //
				columns[13], Utils.nullable(entry.pb.roleset, x -> Utils.quote(Utils.escape(x))), //
				columns[14], Utils.nullable(entry.pb.arg, x -> Utils.quote(Utils.escape(x))), //
				columns[15], Utils.nullable(entry.fn.frame, x -> Utils.quote(Utils.escape(x))), //
				columns[16], Utils.nullable(entry.fn.fetype, x -> Utils.quote(Utils.escape(x))) //
		);
		ps.printf("UPDATE %s SET %s WHERE %s; -- %d%n", table, setClause, whereClause, index + 1);
	}

	private BiConsumer<PmEntry, Integer> makeWnConsumer(final PrintStream ps)
	{
		return (entry, i) -> updateWordSenseRow(ps, //
				names.table("pms"), i, entry, //
				names.column("pms.wordid"), //
				names.column("pms.synsetid"), //
				names.column("pms.word"), //
				names.column("pms.sensekey"));
	}

	private BiConsumer<PmEntry, Integer> makeXnConsumer(final PrintStream ps)
	{
		return (entry, i) -> updateVnPbFnRow(ps, //
				names.table("pms"), i, entry, //
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
	}

	protected void processPmFile(final File file, final BiConsumer<PmEntry, Integer> consumer) throws IOException
	{
		process(file, PmEntry::parse, consumer);
	}
}
