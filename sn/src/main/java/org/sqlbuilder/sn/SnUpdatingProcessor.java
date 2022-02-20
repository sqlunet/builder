package org.sqlbuilder.sn;

import org.sqlbuilder.common.Utils;
import org.sqlbuilder.sn.objects.Collocation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.function.BiConsumer;

public class SnUpdatingProcessor extends SnResolvingProcessor
{
	public SnUpdatingProcessor(final Properties conf) throws IOException, ClassNotFoundException
	{
		super(conf);
	}

	@Override
	public void run() throws IOException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.updateFile("syntagms"))), true, StandardCharsets.UTF_8))
		{
			processSyntagNetFile(ps, new File(snHome, snMain), //
					(collocation, i) -> updateRow(ps, names.table("syntagms"), i, collocation, //
							names.column("syntagms.word1id"), //
							names.column("syntagms.synset1id"), //
							names.column("syntagms.word2id"), //
							names.column("syntagms.synset2id"), //
							names.column("syntagms.sensekey1"), //
							names.column("syntagms.sensekey2") //
					));
		}
	}

	private void updateRow(final PrintStream ps, final String table, final Integer index, final Collocation collocation, final String... columns)
	{
		var r1 = senseResolver.apply(collocation.sensekey1);
		var r2 = senseResolver.apply(collocation.sensekey2);
		if (r1 != null && r2 != null)
		{
			String setClause = String.format("`%s`=%s,`%s`=%s,`%s`=%s,`%s`=%s", //
					columns[0], Utils.nullableInt(r1.getKey()), //
					columns[1], Utils.nullableInt(r1.getValue()), //
					columns[2], Utils.nullableInt(r2.getKey()), //
					columns[3], Utils.nullableInt(r2.getValue()));
			String whereClause = String.format("`%s`=%s AND `%s`= %s", columns[4], Utils.quote(Utils.escape(collocation.sensekey1)), columns[5], Utils.quote(Utils.escape(collocation.sensekey2)));
			ps.printf("UPDATE `%s` SET %s WHERE %s; -- %d%n", table, setClause, whereClause, index + 1);
		}
	}

	protected void processSyntagNetFile(final PrintStream ps, final File file, final BiConsumer<Collocation, Integer> consumer) throws IOException
	{
		ps.printf("-- %s %s%n", file.getName(), serFile);
		process(file, Collocation::parse, consumer);
	}
}
