package org.sqlbuilder.bnc;

import org.sqlbuilder.bnc.objects.BncExtendedRecord;
import org.sqlbuilder.bnc.objects.BncRecord;
import org.sqlbuilder.common.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Stream;

public class BncProcessor extends Processor
{
	protected final File bncHome;

	protected final Names names;

	protected String header;

	protected File outDir;

	protected final Properties conf;

	public BncProcessor(final Properties conf)
	{
		super("bnc");
		this.names = new Names("bnc");
		this.conf = conf;
		this.header = conf.getProperty("bnc_header");
		this.bncHome = new File(conf.getProperty("bnc_home", System.getenv().get("BNCHOME")));
		this.outDir = new File(conf.getProperty("bnc_outdir", "sql/data"));
		if (!this.outDir.exists())
		{
			//noinspection ResultOfMethodCallIgnored
			this.outDir.mkdirs();
		}
	}

	@Override
	public void run() throws IOException
	{
		// main
		String bNCMain = conf.getProperty("bnc_main", "bnc.txt");
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("bncs"))), true, StandardCharsets.UTF_8))
		{
			ps.println("-- " + header);
			processBNCFile(ps, new File(bncHome, bNCMain), names.table("bncs"), names.columns("bncs"), (record, i) -> insertRow(ps, i, record.dataRow()));
		}

		// subfiles
		String bNCSpWr = conf.getProperty("bnc_spwr", "bnc-spoken-written.txt");
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("spwrs"))), true, StandardCharsets.UTF_8))
		{
			ps.println("-- " + header);
			processBNCSubFile(ps, new File(bncHome, bNCSpWr), names.table("spwrs"), names.columns("spwrs"), (record, i) -> insertRow(ps, i, record.dataRow()));
		}

		String bNCConvTask = conf.getProperty("bnc_convtask", "bnc-conv-task.txt");
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("convtasks"))), true, StandardCharsets.UTF_8))
		{
			ps.println("-- " + header);
			processBNCSubFile(ps, new File(bncHome, bNCConvTask), names.table("convtasks"), names.columns("convtasks"), (record, i) -> insertRow(ps, i, record.dataRow()));
		}

		String bNCImagInf = conf.getProperty("bnc_imaginf", "bnc-imag-inf.txt");
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("imaginfs"))), true, StandardCharsets.UTF_8))
		{
			ps.println("-- " + header);
			processBNCSubFile(ps, new File(bncHome, bNCImagInf), names.table("imaginfs"), names.columns("imaginfs"), (record, i) -> insertRow(ps, i, record.dataRow()));
		}
	}

	protected void processBNCFile(final PrintStream ps, final File file, final String table, final String columns, final ThrowingBiConsumer<BncRecord, Integer> consumer) throws IOException
	{
		ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
		process(file, BncRecord::parse, consumer);
		ps.print(';');
	}

	protected void processBNCSubFile(final PrintStream ps, final File file, final String table, final String columns, final ThrowingBiConsumer<BncRecord, Integer> consumer) throws IOException
	{
		ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
		process(file, BncExtendedRecord::parse, consumer);
		ps.print(';');
	}

	protected void process(final File file, final ThrowingFunction<String, BncRecord> producer, final ThrowingBiConsumer<BncRecord, Integer> consumer) throws IOException
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
								Logger.instance.logParseException(BncModule.MODULE_ID, tag, file.getName(), count[1], line, (ParseException) cause);
							}
							else if (cause instanceof NotFoundException)
							{
								Logger.instance.logNotFoundException(BncModule.MODULE_ID, tag, file.getName(), count[1], line, (NotFoundException) cause);
							}
							//							else if (cause instanceof IgnoreException)
							//							{
							//								// ignore
							//							}
						}
						return null;
					}) //
					.filter(Objects::nonNull) //
					.forEach(r -> {
						try
						{
							consumer.acceptThrows(r, count[0]);
							count[0]++;
						}
						catch (NotFoundException nfe)
						{
							Logger.instance.logNotFoundException(BncModule.MODULE_ID, tag, file.getName(), count[1], null, nfe);
						}
						catch (CommonException other)
						{
							System.err.println(other.getCause().getMessage());
						}
					});
		}
	}

	protected void insertRow(PrintStream ps, long index, String values)
	{
		if (index != 0)
		{
			ps.print(",\n");
		}
		ps.printf("(%s)", values);
	}
}
