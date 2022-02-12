package org.sqlbuilder.bnc;

import org.sqlbuilder.bnc.objects.BNCExtendedRecord;
import org.sqlbuilder.bnc.objects.BNCRecord;
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

public class BNCProcessor extends Processor
{
	private final File bncHome;

	private final Names names;

	protected File outDir;

	private final Properties conf;

	public BNCProcessor(final Properties conf) throws IOException, ClassNotFoundException
	{
		super("bnc");
		this.names = new Names("bnc");
		this.conf = conf;
		this.bncHome = new File(conf.getProperty("bnc_home", System.getenv().get("BNCHOME")));
		this.outDir = new File(conf.getProperty("bnc_outdir", "sql/data"));
		if (!this.outDir.exists())
		{
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
			processBNCFile(ps, new File(bncHome, bNCMain), names.table("bncs"), names.columns("bncs"));
		}

		// subfiles
		String bNCSpWr = conf.getProperty("bnc_spwr", "bnc-spoken-written.txt");
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("spwrs"))), true, StandardCharsets.UTF_8))
		{
			processBNCSubFile(ps, new File(bncHome, bNCSpWr), names.table("spwrs"), names.columns("spwrs"));
		}

		String bNCConvTask = conf.getProperty("bnc_convtask", "bnc-conv-task.txt");
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("convtasks"))), true, StandardCharsets.UTF_8))
		{
			processBNCSubFile(ps, new File(bncHome, bNCConvTask), names.table("convtasks"), names.columns("convtasks"));
		}

		String bNCImagInf = conf.getProperty("bnc_imaginf", "bnc-imag-inf.txt");
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("imaginfs"))), true, StandardCharsets.UTF_8))
		{
			processBNCSubFile(ps, new File(bncHome, bNCImagInf), names.table("imaginfs"), names.columns("imaginfs"));
		}
	}

	protected void processBNCFile(final PrintStream ps, final File file, final String table, final String columns) throws IOException
	{
		ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
		try (Stream<String> stream = Files.lines(file.toPath()))
		{
			final int[] count = {0, 0};
			stream //
					.peek(line -> ++count[1]) //
					.filter(line -> !line.isEmpty() && line.charAt(0) == '\t') //
					.map(line -> {
						try
						{
							return BNCRecord.parse(line);
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
					.forEach(r -> {
						String values = r.dataRow();
						insertRow(ps, count[0], values);
						count[0]++;
					});
		}
		ps.print(';');
	}

	protected void processBNCSubFile(final PrintStream ps, final File file, final String table, final String columns) throws IOException
	{
		ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
		try (Stream<String> stream = Files.lines(file.toPath()))
		{
			final int[] count = {0, 0};
			stream //
					.peek(line -> ++count[1]) //
					.filter(line -> !line.isEmpty() && line.charAt(0) == '\t') //
					.map(line -> {
						try
						{
							return BNCExtendedRecord.parse(line);
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
					.forEach(r -> {
						String values = r.dataRow();
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
		ps.printf("(%s)", values);
	}
}
