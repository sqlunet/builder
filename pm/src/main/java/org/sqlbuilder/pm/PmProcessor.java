package org.sqlbuilder.pm;

import org.sqlbuilder.common.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class PmProcessor extends Processor
{
	private final String pMHome;

	protected final Names names;

	protected boolean resolve;

	private final String pMFile;

	protected File outDir;

	protected final Properties conf;

	public PmProcessor(final Properties conf)
	{
		super("pm");
		this.names = new Names("bnc");
		this.resolve = false;
		this.conf = conf;
		this.pMHome = conf.getProperty("pm_home", System.getenv().get("PMHOME"));
		this.pMFile = conf.getProperty("pm_file", System.getenv().get("PredicateMatrix.txt"));
		this.outDir = new File(conf.getProperty("pm_outdir", "sql/data"));
		if (!this.outDir.exists())
		{
			this.outDir.mkdirs();
		}
	}

	@Override
	public void run() throws IOException
	{
		// processPMFile(getPMFile());
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("pmentry"))), true, StandardCharsets.UTF_8))
		{
			processPmFile(ps, getPmFile(), names.table("pmentry"), names.columns("pmentry", resolve), (record, i) -> insertRow(ps, i, record.dataRow()));
		}
	}

	protected void processPmFile(final PrintStream ps, final File file, final String table, final String columns, final BiConsumer<PmEntry, Integer> consumer) throws IOException
	{
		ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
		process(file, PmEntry::parse, consumer);
		ps.print(';');
	}

	private void process(final File file, final ThrowingFunction<String, PmEntry> producer, final BiConsumer<PmEntry, Integer> consumer) throws IOException
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
						catch (CommonException e)
						{
							var cause = e.getCause();
							if (cause instanceof ParseException)
							{
								Logger.instance.logParseException(PmModule.MODULE_ID, this.tag, file.getName(), count[1], line, (ParseException) cause);
							}
							else if (cause instanceof NotFoundException)
							{
								Logger.instance.logNotFoundException(PmModule.MODULE_ID, this.tag, file.getName(), count[1], line, (NotFoundException) cause);
							}
							else if (cause instanceof IgnoreException)
							{
								// ignore
							}
						}
						return null;
					}) //
					.filter(Objects::nonNull) //
					.forEach(r -> {
						consumer.accept(r, count[0]);
						count[0]++;
					});
		}
	}

	private void insertRow(final PrintStream ps, final Integer index, final String values)
	{
		if (index != 0)
		{
			ps.print(",\n");
		}
		ps.printf("(%s)", values);
	}

	@SuppressWarnings("UnusedReturnValue")
	private long processPMFile(final File file) throws IOException
	{
		Progress.traceHeader("pm", file.getName());

		long unreferencedCount = 0;
		long count = 0;

		// iterate on synsets
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file))))
		{
			int lineCount = 0;
			String line;
			while ((line = reader.readLine()) != null)
			{
				lineCount++;
				if (line.isEmpty())
				{
					continue;
				}

				// read
				PmEntry pmEntry;
				try
				{
					pmEntry = PmEntry.parse(line);
				}
				catch (ParseException pe)
				{
					Logger.instance.logParseException(PmModule.MODULE_ID, tag, file.getName(), lineCount, line, pe);
					unreferencedCount++;
					continue;
				}

				// insert
				pmEntry.dataRow();
				count++;

				// trace
				if (Progress.hyperverbose)
				{
					Progress.trace("pm>", line);
					Progress.trace("pm<", pmEntry.toString());
				}
				else
				{
					Progress.trace(count);
				}
			}
			Progress.traceTailer("pm", Long.toString(count));
			Progress.traceTailer("pm unreferenced", Long.toString(unreferencedCount));
		}
		return count;
	}

	private File getPmFile()
	{
		return new File(pMHome + File.separator + pMFile);
	}
}
