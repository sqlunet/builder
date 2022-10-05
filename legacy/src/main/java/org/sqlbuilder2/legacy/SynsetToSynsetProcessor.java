package org.sqlbuilder2.legacy;

import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Names;
import org.sqlbuilder.common.ParseException;
import org.sqlbuilder.common.Processor;
import org.sqlbuilder2.ser.Serialize;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SynsetToSynsetProcessor extends Processor
{
	private final Names names;

	private final Properties conf;

	private final String from;

	private final String to;

	private final File inDir;

	private final File outDir;

	public SynsetToSynsetProcessor(final Properties conf)
	{
		super("sy2sy");
		this.names = new Names("legacy");
		this.conf = conf;
		this.from = conf.getProperty("from");
		this.to = conf.getProperty("to", "XX");
		this.inDir = new File(conf.getProperty("synsets_to_synsets.sourcedir"));
		this.outDir = new File(conf.getProperty("synsets_to_synsets.destdir", "mappings"));
		if (!this.outDir.exists())
		{
			//noinspection ResultOfMethodCallIgnored
			this.outDir.mkdirs();
		}
	}

	@Override
	public void run() throws IOException
	{
		runSer();
		runSql();
	}

	private void runSer() throws IOException
	{
		final String mappings = conf.getProperty("synsets_to_synsets.sourcefile").replaceAll("\\$\\{from}", from).replaceAll("\\$\\{to}", to);
		String outFile = names.file("synsets_to_synsets").replaceAll("\\$\\{from}", from).replaceAll("\\$\\{to}", "XX".equals(to) ? "" : to) + ".ser";

		final Map<Character, Map<Long, Long>> allMaps = new HashMap<>();

		final String legacyNoun = mappings.replaceAll("\\$\\{pos}", "noun");
		var nMap = processSerSynsetToSynsetFile(new File(inDir, legacyNoun));
		allMaps.put('n', nMap);

		final String legacyVerb = mappings.replaceAll("\\$\\{pos}", "verb");
		var vMap = processSerSynsetToSynsetFile(new File(inDir, legacyVerb));
		allMaps.put('v', vMap);

		final String legacyAdj = mappings.replaceAll("\\$\\{pos}", "adj");
		var aMap = processSerSynsetToSynsetFile(new File(inDir, legacyAdj));
		allMaps.put('a', aMap);

		final String legacyAdv = mappings.replaceAll("\\$\\{pos}", "adv");
		var rMap = processSerSynsetToSynsetFile(new File(inDir, legacyAdv));
		allMaps.put('r', rMap);

		Serialize.serialize(allMaps, new File(outDir, outFile));
	}

	private Map<Long, Long> processSerSynsetToSynsetFile(final File file) throws IOException
	{
		try (Stream<String> stream = Files.lines(file.toPath()))
		{
			final long[] count = {0, 0};
			return stream //
					.peek(line -> ++count[1]) //
					.filter(line -> !line.isEmpty() && line.charAt(0) != '#') //
					.map(line -> {
						try
						{
							return SynsetToSynsetMapping.parse(line);
						}
						catch (ParseException pe)
						{
							Logger.instance.logParseException(LegacyModule.MODULE_ID, tag, file.getName(), count[1], line, pe);
						}
						return null;
					}) //
					.filter(Objects::nonNull) // stream of s2s
					.collect(Collectors.toMap(SynsetToSynsetMapping::getFrom, SynsetToSynsetMapping::getTo));
		}
	}

	private void runSql() throws IOException
	{
		final String mappings = conf.getProperty("synsets_to_synsets.sourcefile").replaceAll("\\$\\{from}", from).replaceAll("\\$\\{to}", to);
		String outFile = names.file("synsets_to_synsets").replaceAll("\\$\\{from}", from).replaceAll("\\$\\{to}", "XX".equals(to) ? "" : to) + ".sql";

		String table = names.table("synsets_to_synsets").replaceAll("\\$\\{from}", from).replaceAll("\\$\\{to}", "XX".equals(to) ? "" : to);
		String columns = names.columns("synsets_to_synsets");
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, outFile)), true, StandardCharsets.UTF_8))
		{
			final String legacyNoun = mappings.replaceAll("\\$\\{pos}", "noun");
			processSqlSynsetToSynsetFile(ps, new File(inDir, legacyNoun), table, columns, 'n');
			ps.println();

			final String legacyVerb = mappings.replaceAll("\\$\\{pos}", "verb");
			processSqlSynsetToSynsetFile(ps, new File(inDir, legacyVerb), table, columns, 'v');
			ps.println();

			final String legacyAdj = mappings.replaceAll("\\$\\{pos}", "adj");
			processSqlSynsetToSynsetFile(ps, new File(inDir, legacyAdj), table, columns, 'a');
			ps.println();

			final String legacyAdv = mappings.replaceAll("\\$\\{pos}", "adv");
			processSqlSynsetToSynsetFile(ps, new File(inDir, legacyAdv), table, columns, 'r');
		}
	}

	private void processSqlSynsetToSynsetFile(final PrintStream ps, final File file, final String table, @SuppressWarnings("SameParameterValue") final String columns, final char pos) throws IOException
	{
		ps.printf("-- %s%n", names.header("synsets_to_synsets").replaceAll("\\$\\{from}", from).replaceAll("\\$\\{to}", to));
		ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
		try (Stream<String> stream = Files.lines(file.toPath()))
		{
			final long[] count = {0, 0};
			stream //
					.peek(line -> ++count[1]) //
					.filter(line -> !line.isEmpty() && line.charAt(0) != '#') //
					.map(line -> {
						try
						{
							return SynsetToSynsetMapping.parse(line);
						}
						catch (ParseException pe)
						{
							Logger.instance.logParseException(LegacyModule.MODULE_ID, tag, file.getName(), count[1], line, pe);
						}
						return null;
					}) //
					.filter(Objects::nonNull) //
					.forEach(s2s -> {
						String values = s2s.dataRow();
						insertRow(ps, count[0], pos, values);
						count[0]++;
					});
		}
		ps.print(';');
	}

	private void insertRow(final PrintStream ps, final long index, final char pos, final String values)
	{
		if (index != 0)
		{
			ps.print(",\n");
		}
		ps.printf("('%c',%s)", pos, values);
	}
}
