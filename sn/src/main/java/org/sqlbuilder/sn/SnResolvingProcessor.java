package org.sqlbuilder.sn;

import org.sqlbuilder.common.Utils;
import org.sqlbuilder.sn.objects.Collocation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.function.BiConsumer;

public class SnResolvingProcessor extends SnProcessor
{
	protected final String serFile;

	protected final SnSensekeyResolver senseResolver;

	public SnResolvingProcessor(final Properties conf) throws IOException, ClassNotFoundException
	{
		super(conf);

		// outdir
		this.outDir = new File(conf.getProperty("sn_outdir_resolved", "sql/data_resolved"));
		if (!this.outDir.exists())
		{
			//noinspection ResultOfMethodCallIgnored
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
			processSyntagNetFile(ps, new File(snHome, snMain), names.table("syntagms"), names.columns("syntagms", true), (collocation, i) -> {

				var nr = collocation.dataRow();
				var r1 = senseResolver.apply(collocation.sensekey1);
				var r2 = senseResolver.apply(collocation.sensekey2);
				if (r1 != null && r2 != null)
				{
					var values = String.format("%s,%s,%s,%s,%s", nr, Utils.nullableInt(r1.getKey()), Utils.nullableInt(r1.getValue()), Utils.nullableInt(r2.getKey()), Utils.nullableInt(r2.getValue()));
					insertRow(ps, i, values);
				}
			});
		}
	}

	@Override
	protected void processSyntagNetFile(final PrintStream ps, final File file, final String table, final String columns, final BiConsumer<Collocation, Integer> consumer) throws IOException
	{
		ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
		process(file, Collocation::parse, consumer);
		ps.print(';');
	}
}
