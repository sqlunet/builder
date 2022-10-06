package org.sqlbuilder.sumo;

import org.sqlbuilder.common.NotFoundException;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.sumo.joins.Term_Synset;
import org.sqlbuilder.sumo.objects.Formula;
import org.sqlbuilder.sumo.objects.SUFile;
import org.sqlbuilder.sumo.objects.Term;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Collection;
import java.util.Properties;

public class SumoUpdatingProcessor extends SumoResolvingProcessor
{
	public SumoUpdatingProcessor(final Properties conf) throws IOException, ClassNotFoundException
	{
		super(conf);

		// output
		this.outDir = new File(conf.getProperty("su_outdir_updated", "sql/data_updated"));
		if (!this.outDir.exists())
		{
			//noinspection ResultOfMethodCallIgnored
			this.outDir.mkdirs();
		}
	}
	@Override
	public void run() throws IOException
	{
		new KBLoader().load();
		collectTerms(KBLoader.kb);
		collectSynsets(this.inDir + File.separator + SUMO_TEMPLATE, System.err);

		try ( //
		      var ignored = Term.COLLECTOR.open(); //
		) //
		{
			// terms_senses
			try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("terms_synsets"))), true, StandardCharsets.UTF_8))
			{
				ps.println("-- " + header);
				processSynsets(ps, Term_Synset.SET, names.table("terms_synsets"), columns);
			}
		}
	}

	@Override
	public void processSynsets(final PrintStream ps, final Collection<Term_Synset> terms_synsets, final String table, final String columns)
	{
		if (terms_synsets.size() > 0)
		{
			final String colMapId = names.column("terms_synsets.mapid");
			final String colPosId = names.column("terms_synsets.posid");
			final String colSynsetId = names.column("terms_synsets.synsetid");
			int i = 0;
			for (final Term_Synset map : terms_synsets)
			{
				updateRow(ps, table, ++i, map, colSynsetId, colMapId, colPosId);
			}
		}
	}

	private void updateRow(final PrintStream ps, final String table, final Integer index, final Term_Synset map, final String... columns)
	{
		var r = synsetResolver.apply(map.posId, map.synsetId);
		if (r != null)
		{
			String setClause = String.format("%s=%s", columns[0], Utils.nullableLong(r));
			String whereClause = String.format("%s=%s AND %s='%s'", //
					columns[1], map.synsetId, //
					columns[2], map.posId //
			);
			ps.printf("UPDATE %s SET %s WHERE %s; -- %d %s%n", table, setClause, whereClause, index + 1, map.comment());
		}
	}
}
