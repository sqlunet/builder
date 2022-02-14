package org.sqlbuilder.sn;

import org.sqlbuilder.common.*;
import org.sqlbuilder.sn.objects.Collocation;
import org.sqlbuilder.sn.objects.ResolvingCollocation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.Objects;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class SnResolvingProcessor extends SnProcessor
{
	protected final String serFile;

	private final SnSensekeyResolver senseResolver;

	public SnResolvingProcessor(final Properties conf) throws IOException, ClassNotFoundException
	{
		super(conf);

		// outdir
		this.outDir = new File(conf.getProperty("sn_outdir_resolved", "sql/data_resolved"));
		if (!this.outDir.exists())
		{
			this.outDir.mkdirs();
		}

		// resolver
		this.resolve = true;
		this.serFile = conf.getProperty("sense_nids");
		this.senseResolver = new SnSensekeyResolver(this.serFile);
	}

	@Override
	public void run() throws IOException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("syntagms"))), true, StandardCharsets.UTF_8))
		{
			processSyntagNetFile(ps, new File(snHome, snMain), names.table("syntagms"), names.columns("syntagms", true), (collocation, i) -> insertRow(ps, i, collocation.dataRow()));
		}
	}

	@Override
	protected void processSyntagNetFile(final PrintStream ps, final File file, final String table, final String columns, final BiConsumer<Collocation, Integer> consumer) throws IOException
	{
		ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
		process(file, ResolvingCollocation::parse, consumer);
		ps.print(';');
	}
	protected void process(final File file, final ThrowingFunction<String, ResolvingCollocation> producer, final BiConsumer<Collocation, Integer> consumer) throws IOException
	{
		try (Stream<String> stream = Files.lines(file.toPath()))
		{
			final int[] count = {0, 0};
			stream //
					.peek(line -> ++count[1]) //
					.filter(line -> !line.isEmpty() && line.charAt(0) != '#') //
					.map(line -> {
						try
						{
							return producer.applyThrows(line);
						}
						catch (ParseException pe)
						{
							Logger.instance.logParseException(SnModule.MODULE_ID, this.tag, "parse", file.getName(), count[1], line, null, pe);
						}
						catch (NotFoundException nfe)
						{
							Logger.instance.logNotFoundException(SnModule.MODULE_ID, this.tag, "parse", file.getName(), count[1], line, null, nfe);
						}
						catch (IgnoreException ignoreException)
						{
						}
						return null;
					}) //
					.filter(Objects::nonNull) //
					.sorted(Comparator.comparing(ResolvingCollocation::getWord1).thenComparing(ResolvingCollocation::getWord2)) //
					.filter(collocation -> collocation.resolve(sensekeyResolver, senseResolver)) //
					.sorted(Comparator.comparing(ResolvingCollocation::getSensekey1).thenComparing(ResolvingCollocation::getSensekey2)) //
					.forEach(collocation -> {
						consumer.accept(collocation, count[0]);
						count[0]++;
					});
		}
	}
}
