package org.sqlbuilder.bnc;

import org.sqlbuilder.bnc.objects.BNCExtendedResolvingRecord;
import org.sqlbuilder.bnc.objects.BNCRecord;
import org.sqlbuilder.bnc.objects.BNCResolvingRecord;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.function.BiConsumer;

public class BNCUpdatingProcessor extends BNCResolvingProcessor
{
	public BNCUpdatingProcessor(final Properties conf) throws IOException, ClassNotFoundException
	{
		super(conf);
	}

	@Override
	public void run() throws IOException
	{
		// main
		String bNCMain = conf.getProperty("bnc_main", "bnc.txt");
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.updateFile("bncs"))), true, StandardCharsets.UTF_8))
		{
			processBNCFile(ps, new File(bncHome, bNCMain), (record, i) -> updateRow(ps, names.table("bncs"), i, record.updateRow(names.get("bncs.wordid"), names.get("bncs.word"))));
		}

		// subfiles
		String bNCSpWr = conf.getProperty("bnc_spwr", "bnc-spoken-written.txt");
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.updateFile("spwrs"))), true, StandardCharsets.UTF_8))
		{
			processBNCSubFile(ps, new File(bncHome, bNCSpWr), (record, i) -> updateRow(ps, names.table("spwrs"), i, record.updateRow(names.get("spwrs.wordid"), names.get("spwrs.word"))));
		}

		String bNCConvTask = conf.getProperty("bnc_convtask", "bnc-conv-task.txt");
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.updateFile("convtasks"))), true, StandardCharsets.UTF_8))
		{
			processBNCSubFile(ps, new File(bncHome, bNCConvTask), (record, i) -> updateRow(ps, names.table("convtasks"), i, record.updateRow(names.get("convtasks.wordid"), names.get("convtasks.word"))));
		}

		String bNCImagInf = conf.getProperty("bnc_imaginf", "bnc-imag-inf.txt");
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.updateFile("imaginfs"))), true, StandardCharsets.UTF_8))
		{
			processBNCSubFile(ps, new File(bncHome, bNCImagInf), (record, i) -> updateRow(ps, names.table("imaginfs"), i, record.updateRow(names.get("imaginfs.wordid"), names.get("imaginfs.word"))));
		}
	}

	private void processBNCFile(final PrintStream ps, final File file, final BiConsumer<BNCRecord, Integer> consumer) throws IOException
	{
		ps.printf("-- %s %s%n", file.getName(), this.serFile);
		process(file, BNCResolvingRecord::parse, consumer);
	}

	private void processBNCSubFile(final PrintStream ps, final File file, final BiConsumer<BNCRecord, Integer> consumer) throws IOException
	{
		ps.printf("-- %s %s%n", file.getName(), this.serFile);
		process(file, BNCExtendedResolvingRecord::parse, consumer);
	}

	private void updateRow(final PrintStream ps, final String table, final Integer index, final String updateRow)
	{
		ps.printf("UPDATE `%s` SET %s; -- %d%n", table, updateRow, index + 1);
	}
}
