package org.sqlbuilder.sn;

import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.ParseException;
import org.sqlbuilder.common.Processor;
import org.sqlbuilder.sn.objects.Collocation;
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
	protected final File snHome;

	protected final File outDir;

	protected final Properties conf;

	private Map<Triplet<String, Character, Long>, String> toSenseKeys;

	protected final Function<Triplet<String, Character, Long>, String> sensekeyResolver = lpo -> toSenseKeys.get(lpo);

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

	protected void processSyntagNetFile(final PrintStream ps, final File file, final String table, final String columns) throws IOException
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
							return Collocation.parse(line);
						}
						catch (ParseException pe)
						{
							Logger.instance.logParseException(SnModule.MODULE_ID, this.tag, "parse", file.getName(), count[1], line, null, pe);
						}
						return null;
					}) //
					.filter(Objects::nonNull) //
					//.sorted(Comparator.comparing(Collocation::getWord1).thenComparing(Collocation::getWord2)) //
					.filter(collocation -> collocation.resolve(sensekeyResolver)) //
					.sorted(Comparator.comparing(Collocation::getSensekey1, Comparator.nullsFirst(Comparator.naturalOrder())).thenComparing(Collocation::getSensekey2, Comparator.nullsFirst(Comparator.naturalOrder()))) //
					.forEach(sp -> {
						String values = sp.dataRow();
						insertRow(ps, count[0], values);
						count[0]++;
					});
		}
		ps.print(';');
	}

	protected void insertRow(PrintStream ps, long index, String values)
	{
		if (index != 0)
		{
			ps.print(",\n");
		}
		ps.printf("(%d,%s)", index + 1, values);
	}
}
