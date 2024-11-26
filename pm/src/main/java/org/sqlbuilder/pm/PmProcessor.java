package org.sqlbuilder.pm;

import org.sqlbuilder.annotations.ProvidesIdTo;
import org.sqlbuilder.common.*;
import org.sqlbuilder.pm.objects.PmEntry;
import org.sqlbuilder.pm.objects.PmPredicate;
import org.sqlbuilder.pm.objects.PmRole;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class PmProcessor extends Processor
{
	protected final String pMHome;

	protected final String pMFile;

	protected final Names names;
	protected final File outDir;
	protected final Properties conf;
	protected String header;

	public PmProcessor(final Properties conf)
	{
		super("pm");
		this.names = new Names("pm");
		this.conf = conf;
		this.header = conf.getProperty("pm_header");
		this.pMHome = conf.getProperty("pm_home", System.getenv().get("PMHOME"));
		this.pMFile = conf.getProperty("pm_file", System.getenv().get("PredicateMatrix.txt"));
		this.outDir = new File(conf.getProperty("pm_outdir", "sql/data"));
		if (!this.outDir.exists())
		{
			//noinspection ResultOfMethodCallIgnored
			this.outDir.mkdirs();
		}
	}

	protected static <T> void process(final File file, final ThrowingFunction<String, T> producer, final BiConsumer<T, Integer> consumer) throws IOException
	{
		try (Stream<String> stream = Files.lines(file.toPath()))
		{
			final int[] count = {0, 0};
			stream //
					.peek(line -> ++count[1]) //
					.filter(line -> !line.isEmpty() && line.charAt(0) != '\t') //
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
								Logger.instance.logParseException(PmModule.MODULE_ID, "pm", file.getName(), count[1], line, (ParseException) cause);
							}
							else if (cause instanceof NotFoundException)
							{
								Logger.instance.logNotFoundException(PmModule.MODULE_ID, "pm", file.getName(), count[1], line, (NotFoundException) cause);
							}
							//	else if (cause instanceof IgnoreException)
							//	{
							//		// ignore
							//	}
						}
						return null;
					}) //
					.filter(Objects::nonNull) //
					.forEach(r -> {
						if (consumer != null)
						{
							consumer.accept(r, count[0]);
						}
						count[0]++;
					});
		}
	}

	@Override
	public void run() throws IOException
	{
		var inputFile = new File(pMHome, pMFile);
		process(inputFile, PmRole::parse, null);

		try (@ProvidesIdTo(type = PmPredicate.class) var ignored1 = PmPredicate.COLLECTOR.open())
		{
			Insert.insert2(PmPredicate.COLLECTOR, PmPredicate.COLLECTOR, new File(outDir, names.file("predicates")), names.table("predicates"), names.columns("predicates"), header);

			try (@ProvidesIdTo(type = PmRole.class) var ignored2 = PmRole.COLLECTOR.open())
			{
				Insert.insert2(PmRole.COLLECTOR, PmRole.COLLECTOR, new File(outDir, names.file("roles")), names.table("roles"), names.columns("roles"), header);

				try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("pms"))), true, StandardCharsets.UTF_8))
				{
					ps.println("-- " + header);
					processPmFile(ps, inputFile, names.table("pms"), names.columns("pms", false), (role, i) -> insertRow(ps, i, role.dataRow()));
				}
			}
		}
	}

	protected void processPmFile(final PrintStream ps, final File file, final String table, final String columns, final BiConsumer<PmEntry, Integer> consumer) throws IOException
	{
		ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
		process(file, PmEntry::parse, consumer);
		ps.print(';');
	}

	protected void insertRow(final PrintStream ps, final Integer index, final String values)
	{
		if (index != 0)
		{
			ps.print(",\n");
		}
		ps.printf("(%d,%s)", index + 1, values);
	}
}
