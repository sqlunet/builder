package org.sqlbuilder.bnc;

import org.sqlbuilder.bnc.objects.BNCExtendedRecord;
import org.sqlbuilder.bnc.objects.BNCExtendedResolvingRecord;
import org.sqlbuilder.bnc.objects.BNCRecord;
import org.sqlbuilder.bnc.objects.BNCResolvingRecord;
import org.sqlbuilder.common.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Stream;

public class BNCResolvingProcessor extends BNCProcessor
{
	private Map<String, Integer> map;

	private final Function<String, Integer> resolver = w -> map.get(w);

	public BNCResolvingProcessor(final Properties conf) throws IOException, ClassNotFoundException
	{
		super(conf);

		// resolve
		this.outDir = new File(conf.getProperty("bnc_outdir_resolved", "sql/data"));

		// resolve
		File wordNIDS = new File(conf.getProperty("word_nids"));
		this.map = DeSerializeNIDs.deserializeNIDs(wordNIDS);
	}

	@Override
	protected void processBNCFile(final PrintStream ps, final File file, final String table, final String columns) throws IOException
	{
		ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
		try (Stream<String> stream = Files.lines(file.toPath()))
		{
			final int[] count = {0,0};
			stream //
					.peek(line -> ++count[1]) //
					.filter(line -> !line.isEmpty() && line.charAt(0) == '\t') //
					.map(line -> {
						try
						{
							return BNCResolvingRecord.parse(line);
						}
						catch (ParseException pe)
						{
							Logger.instance.logParseException(BNCModule.MODULE_ID, this.tag, "parse", file.getName(), count[1], line, null, pe);
						}
						catch (NotFoundException nfe)
						{
							Logger.instance.logNotFoundException(BNCModule.MODULE_ID, this.tag, "parse", file.getName(), count[1], line, null, nfe);
						}
						catch (IgnoreException ignored)
						{
						}
						return null;
					}) //
					.filter(Objects::nonNull) //
					.filter(r -> r.resolve(resolver)) //
					.forEach(r -> {
						String values = r.dataRow();
						insertRow(ps, count[0], values);
						count[0]++;
					});
		}
		ps.print(';');
	}

	@Override
	protected void processBNCSubFile(final PrintStream ps, final File file, final String table, final String columns) throws IOException
	{
		ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
		try (Stream<String> stream = Files.lines(file.toPath()))
		{
			final int[] count = {0,0};
			stream //
					.peek(line -> ++count[1]) //
					.filter(line -> !line.isEmpty() && line.charAt(0) == '\t') //
					.map(line -> {
						try
						{
							return BNCExtendedResolvingRecord.parse(line);
						}
						catch (ParseException pe)
						{
							Logger.instance.logParseException(BNCModule.MODULE_ID, this.tag, "parse", file.getName(), count[1], line, null, pe);
						}
						catch (NotFoundException nfe)
						{
							Logger.instance.logNotFoundException(BNCModule.MODULE_ID, this.tag, "parse", file.getName(), count[1], line, null, nfe);
						}
						catch (IgnoreException ignored)
						{
						}
						return null;
					}) //
					.filter(Objects::nonNull) //
					.filter(r -> r.resolve(resolver)) //
					.forEach(r -> {
						String values = r.dataRow();
						insertRow(ps, count[0], values);
						count[0]++;
					});
		}
		ps.print(';');
	}
}
