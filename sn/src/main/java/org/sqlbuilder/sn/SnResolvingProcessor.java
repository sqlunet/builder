package org.sqlbuilder.sn;

import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.ParseException;
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
	private final SnResolver senseResolver;

	public SnResolvingProcessor(final Properties conf) throws IOException, ClassNotFoundException
	{
		super(conf);

		// outdir
		this.outDir = new File(conf.getProperty("bnc_outdir_resolved", "sql/data"));

		// resolver
		senseResolver = new SnResolver(conf);
	}

	@Override
	public void run() throws IOException
	{
		final String snMain = conf.getProperty("sn_file", "SYNTAGNET.txt");
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("syntagms"))), true, StandardCharsets.UTF_8))
		{
			processSyntagNetFile(ps, new File(snHome, snMain), names.table("syntagms"), names.columns("syntagms"), (collocation, i) -> insertRow(ps, i, collocation.dataRow()));
		}
	}

	@Override
	protected void processSyntagNetFile(final PrintStream ps, final File file, final String table, final String columns, final BiConsumer<Collocation, Long> bc) throws IOException
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
							return ResolvingCollocation.parse(line);
						}
						catch (ParseException pe)
						{
							Logger.instance.logParseException(SnModule.MODULE_ID, this.tag, "parse", file.getName(), count[1], line, null, pe);
						}
						return null;
					}) //
					.filter(Objects::nonNull) //
					.sorted(Comparator.comparing(ResolvingCollocation::getWord1).thenComparing(ResolvingCollocation::getWord2)) //
					.filter(collocation -> collocation.resolve(sensekeyResolver, senseResolver)) //
					.sorted(Comparator.comparing(ResolvingCollocation::getSensekey1).thenComparing(ResolvingCollocation::getSensekey2)) //
					.forEach(collocation -> {
						bc.accept(collocation, count[0]);
						count[0]++;
					});
		}
		ps.print(';');
	}
}
