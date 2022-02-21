package org.sqlbuilder.sn;

import org.sqlbuilder.common.*;
import org.sqlbuilder.sn.objects.Collocation;
import org.sqlbuilder2.ser.DeSerialize;
import org.sqlbuilder2.ser.Triplet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class SnProcessor extends Processor
{
	protected final File snHome;

	protected final String snMain;

	protected final Names names;

	protected boolean resolve;

	protected File outDir;

	protected final Properties conf;

	private Map<Triplet<String, Character, Long>, String> toSenseKeys;

	protected final Function<Triplet<String, Character, Long>, String> sensekeyResolver = lpo -> toSenseKeys.get(lpo);

	public SnProcessor(final Properties conf) throws IOException, ClassNotFoundException
	{
		super("sn");
		this.conf = conf;
		this.names = new Names("sn");
		this.resolve = false;
		this.snHome = new File(conf.getProperty("sn_home", System.getenv().get("SNHOME")));
		this.snMain = conf.getProperty("sn_file", "SYNTAGNET.txt");
		this.outDir = new File(conf.getProperty("sn_outdir", "sql/data"));
		if (!this.outDir.exists())
		{
			this.outDir.mkdirs();
		}

		// resolve
		File toSensekeys = new File(conf.getProperty("to_sensekeys"));
		this.toSenseKeys = DeSerialize.deserialize(toSensekeys);
	}

	@Override
	public void run() throws IOException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("syntagms"))), true, StandardCharsets.UTF_8))
		{
			processSyntagNetFile(ps, new File(snHome, snMain), names.table("syntagms"), names.columns("syntagms", resolve), (collocation, i) -> insertRow(ps, i, collocation.dataRow()));
		}
	}

	protected void processSyntagNetFile(final PrintStream ps, final File file, final String table, final String columns, final BiConsumer<Collocation, Integer> consumer) throws IOException
	{
		ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
		process(file, Collocation::parse, consumer);
		ps.print(';');
	}

	protected void process(final File file, final ThrowingFunction<String, Collocation> producer, final BiConsumer<Collocation, Integer> consumer) throws IOException
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
						catch (CommonException e)
						{
							var cause = e.getCause();
							if (cause instanceof ParseException)
							{
								Logger.instance.logParseException(SnModule.MODULE_ID, tag, file.getName(), count[1], line, (ParseException) cause);
							}
							else if (cause instanceof NotFoundException)
							{
								Logger.instance.logNotFoundException(SnModule.MODULE_ID, tag, file.getName(), count[1], line, (NotFoundException) cause);
							}
							else if (cause instanceof IgnoreException)
							{
								// ignore
							}
						}
						return null;
					}) //
					.filter(Objects::nonNull) //
					//.sorted(Comparator.comparing(Collocation::getWord1).thenComparing(Collocation::getWord2)) //
					.filter(collocation -> collocation.resolveOffsets(sensekeyResolver)) //
					.sorted(Comparator.comparing(Collocation::getSensekey1, Comparator.nullsFirst(Comparator.naturalOrder())).thenComparing(Collocation::getSensekey2, Comparator.nullsFirst(Comparator.naturalOrder()))) //
					.forEach(collocation -> {
						consumer.accept(collocation, count[0]);
						count[0]++;
					});
		}
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
