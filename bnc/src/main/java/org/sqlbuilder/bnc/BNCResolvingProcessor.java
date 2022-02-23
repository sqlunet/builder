package org.sqlbuilder.bnc;

import org.sqlbuilder.bnc.objects.BNCExtendedRecord;
import org.sqlbuilder.bnc.objects.BNCRecord;
import org.sqlbuilder.common.NotFoundException;
import org.sqlbuilder.common.ThrowingBiConsumer;
import org.sqlbuilder.common.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class BNCResolvingProcessor extends BNCProcessor
{
	protected final String serFile;

	protected final BncWordResolver wordResolver;

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
		this.serFile = conf.getProperty("word_nids");
		this.wordResolver = new BncWordResolver(this.serFile);
	}

	@Override
	public void run() throws IOException
	{
		// main
		String bNCMain = conf.getProperty("bnc_main", "bnc.txt");
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("bncs"))), true, StandardCharsets.UTF_8))
		{
			processBNCFile(ps, new File(bncHome, bNCMain), names.table("bncs"), names.columns("bncs", true), (record, i) -> resolveAndInsert(ps, record, i));
		}

		// subfiles
		String bNCSpWr = conf.getProperty("bnc_spwr", "bnc-spoken-written.txt");
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("spwrs"))), true, StandardCharsets.UTF_8))
		{
			processBNCSubFile(ps, new File(bncHome, bNCSpWr), names.table("spwrs"), names.columns("spwrs", true), (record, i) -> resolveAndInsert(ps, record, i));
		}

		String bNCConvTask = conf.getProperty("bnc_convtask", "bnc-conv-task.txt");
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("convtasks"))), true, StandardCharsets.UTF_8))
		{
			processBNCSubFile(ps, new File(bncHome, bNCConvTask), names.table("convtasks"), names.columns("convtasks", true), (record, i) -> resolveAndInsert(ps, record, i));
		}

		String bNCImagInf = conf.getProperty("bnc_imaginf", "bnc-imag-inf.txt");
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("imaginfs"))), true, StandardCharsets.UTF_8))
		{
			processBNCSubFile(ps, new File(bncHome, bNCImagInf), names.table("imaginfs"), names.columns("imaginfs", true), (record, i) -> resolveAndInsert(ps, record, i));
		}
	}

	private void resolveAndInsert(final PrintStream ps, final BNCRecord record, final int i) throws NotFoundException
	{
		var nr = record.dataRow();
		var r = wordResolver.apply(record.word);
		if (r != null)
		{
			var values = String.format("%s,%s", nr, Utils.nullableInt(r));
			insertRow(ps, i, values);
			return;
		}
		throw new NotFoundException(record.word);
	}

	@Override
	protected void processBNCFile(final PrintStream ps, final File file, final String table, final String columns, final ThrowingBiConsumer<BNCRecord, Integer> consumer) throws IOException
	{
		ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
		process(file, BNCRecord::parse, consumer);
		ps.print(';');
	}

	@Override
	protected void processBNCSubFile(final PrintStream ps, final File file, final String table, final String columns, final ThrowingBiConsumer<BNCRecord, Integer> consumer) throws IOException
	{
		ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
		process(file, BNCExtendedRecord::parse, consumer);
		ps.print(';');
	}
}
