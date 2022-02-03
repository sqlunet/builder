package org.sqlbuilder.sn;

import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.ParseException;
import org.sqlbuilder.common.Processor;
import org.sqlbuilder2.legacy.DeSerialize;
import org.sqlbuilder2.legacy.Triplet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Stream;

public class SnProcessor extends Processor
{
	private final File snHome;

	private final File outDir;

	private final Properties conf;

	private Map<Triplet<String, Character, Long>, String> toSenseKeys;

	private Map<String, SimpleEntry<Integer, Integer>> senseNIDs;

	private final Function<Triplet<String, Character, Long>, String> sensekeyResolver = lpo -> toSenseKeys.get(lpo);

	private final Function<String, SimpleEntry<Integer, Integer>> senseResolver = sk -> senseNIDs.get(sk);

	public SnProcessor(final Properties conf) throws IOException, ClassNotFoundException
	{
		super("sn");
		this.conf = conf;
		this.snHome = new File(conf.getProperty("snhome", System.getenv().get("SNHOME")));
		this.outDir = new File(conf.getProperty("snoutdir", "sql/data"));
		if (!this.outDir.exists())
		{
			this.outDir.mkdirs();
		}

		// resolve
		File toSensekeys = new File(conf.getProperty("tosensekeys"));
		this.toSenseKeys = DeSerialize.deserialize(toSensekeys);

		File senseNIDS = new File(conf.getProperty("sensenids"));
		this.senseNIDs = DeSerialize.deserialize(senseNIDS);
	}

	@Override
	public void run() throws IOException
	{
		final String snMain = conf.getProperty("snfile", "SYNTAGNET.txt");
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, Names.SN.FILE)), true, StandardCharsets.UTF_8))
		{
			processSyntagNetFile(ps, new File(snHome, snMain), Names.SN.TABLE, Names.SN.COLUMNS);
		}
	}

	private void processSyntagNetFile(final PrintStream ps, final File file, final String table, final String columns) throws IOException
	{
		ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
		try (Stream<String> stream = Files.lines(file.toPath()))
		{
			final long[] count = new long[2];
			stream //
					.peek(line -> ++count[1]) //
					.filter(line -> !line.isEmpty() && line.charAt(0) != '#') //
					.map(line -> {
						try
						{
							return SnCollocation.parse(line);
						}
						catch (ParseException pe)
						{
							Logger.instance.logParseException(SnModule.MODULE_ID, this.tag, "parse", file.getName(), count[1], line, null, pe);
						}
						return null;
					}) //
					.filter(Objects::nonNull) //
					.sorted(Comparator.comparing(SnCollocation::getWord1).thenComparing(SnCollocation::getWord2)) //
					.filter(collocation -> collocation.resolve(sensekeyResolver, senseResolver)) //
					.sorted(Comparator.comparing(SnCollocation::getSensekey1).thenComparing(SnCollocation::getSensekey2)) //
					.forEach(sp -> {
						String values = sp.dataRow();
						insertRow(ps, count[0], values);
						count[0]++;
					});
		}
		ps.print(';');
	}

	private void insertRow(PrintStream ps, long index, String values)
	{
		if (index != 0)
		{
			ps.print(",\n");
		}
		ps.printf("(%d,%s)", index + 1, values);
	}
}
