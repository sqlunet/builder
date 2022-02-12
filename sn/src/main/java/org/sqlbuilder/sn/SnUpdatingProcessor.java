package org.sqlbuilder.sn;

import org.sqlbuilder.sn.objects.Collocation;
import org.sqlbuilder.sn.objects.ResolvingCollocation;

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
			processSyntagNetFile(ps, new File(snHome, snMain), (collocation, i) -> updateRow(ps, names.table("syntagms"), i, collocation.updateRow( //
					names.get("syntagms.word1id"), //
					names.get("syntagms.synset1id"), //
					names.get("syntagms.word2id"), //
					names.get("syntagms.synset2id"), //
					names.get("syntagms.sensekey1"), //
					names.get("syntagms.sensekey2") //
			)));
		}
	}

	private void updateRow(final PrintStream ps, final String table, final Integer index, final String updateRow)
	{
		ps.printf("UPDATE `%s` SET %s; -- %d%n", table, updateRow , index + 1);
	}

	protected void processSyntagNetFile(final PrintStream ps, final File file, final BiConsumer<Collocation, Integer> consumer) throws IOException
	{
		ps.printf("-- %s %s%n", file.getName(), serFile);
		process(file, ResolvingCollocation::parse, consumer);
	}

	/*
	public void updates()
	{
		Names names = new Names("sn");
		PrintStream ps = System.out;
		map.forEach((k, v) -> ps.printf("UPDATE `%s` SET `%s`=%d,`%s`=%d WHERE `%s` = '%s';%n", //
				names.table("syntagms"), //
				names.get("syntagms.word1id"), v.getKey(), //
				names.get("syntagms.synset1id"), v.getValue(),  //
				names.get("syntagms.sensekey1"), k));
		map.forEach((k, v) -> ps.printf("UPDATE `%s` SET `%s`=%d,`%s`=%d WHERE `%s` = '%s';%n", //
				names.table("syntagms"), //
				names.get("syntagms.word2id"), v.getKey(), //
				names.get("syntagms.synset2id"), v.getValue(),  //
				names.get("syntagms.sensekey2"), k));

	}
	 */
}
