package org.sqlbuilder.bnc;

import org.sqlbuilder.bnc.objects.BNCExtendedResolvingRecord;
import org.sqlbuilder.bnc.objects.BNCRecord;
import org.sqlbuilder.bnc.objects.BNCResolvingRecord;
import org.sqlbuilder.common.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class BNCResolvingProcessor extends BNCProcessor
{
	protected final String serFile;

	private final BncWordResolver resolver;

	public BNCResolvingProcessor(final Properties conf) throws IOException, ClassNotFoundException
	{
		super(conf);

		// output
		this.outDir = new File(conf.getProperty("bnc_outdir_resolved", "sql/data_resolved"));
		if (!this.outDir.exists())
		{
			this.outDir.mkdirs();
		}

		// resolve
		this.resolve = true;
		this.serFile = conf.getProperty("word_nids");
		this.resolver = new BncWordResolver(this.serFile);
	}

	@Override
	protected void processBNCFile(final PrintStream ps, final File file, final String table, final String columns, final BiConsumer<BNCRecord, Integer> consumer) throws IOException
	{
		ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
		process(file, BNCResolvingRecord::parse, consumer);
		ps.print(';');
	}

	@Override
	protected void processBNCSubFile(final PrintStream ps, final File file, final String table, final String columns, final BiConsumer<BNCRecord, Integer> consumer) throws IOException
	{
		ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
		process(file, BNCExtendedResolvingRecord::parse, consumer);
		ps.print(';');
	}

	protected void process(final File file, final ThrowingFunction<String, BNCResolvingRecord> producer, final BiConsumer<BNCRecord, Integer> consumer) throws IOException
	{
		try (Stream<String> stream = Files.lines(file.toPath()))
		{
			final int[] count = {0, 0};
			stream //
					.peek(line -> ++count[1]) //
					.filter(line -> !line.isEmpty() && line.charAt(0) == '\t') //
					.map(line -> {
						try
						{
							return producer.applyThrows(line);
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
						consumer.accept(r, count[0]);
						count[0]++;
					});
		}
	}
}