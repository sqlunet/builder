package org.sqlbuilder.sumo;

import org.sqlbuilder.sumo.joins.Term_Synset;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Properties;

public class SumoResolvingProcessor extends SumoProcessor
{
	protected final SuSynsetResolver synsetResolver;

	public SumoResolvingProcessor(final Properties conf) throws IOException, ClassNotFoundException
	{
		super(conf);

		// header
		this.header += "\n-- " + conf.getProperty("wn_resolve_against");

		// columns
		this.columns = names.columns("terms_synsets", true);

		// outdir
		this.outDir = new File(conf.getProperty("su_outdir_resolved", "sql/data_resolved"));
		if (!this.outDir.exists())
		{
			//noinspection ResultOfMethodCallIgnored
			this.outDir.mkdirs();
		}

		// resolver
		this.resolve = true;
		String serFile = conf.getProperty("synsets30_to_synsets");
		this.synsetResolver = new SuSynsetResolver(serFile);
	}

	@Override
	public void processSynsets(final PrintStream ps, final Collection<Term_Synset> terms_synsets, final String table, final String columns)
	{
		int n = terms_synsets.size();
		if (n > 0)
		{
			int i = 0;
			ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
			for (final Term_Synset map : terms_synsets)
			{
				String row = map.dataRow();
				char posId = map.posId;
				long synsetId = map.synsetId;
				Long resolvedSynsetId = synsetResolver.apply(posId, synsetId);
				String comment = map.comment();
				ps.printf("(%s,%s)%s -- %s%n", row, Utils.nullableLong(resolvedSynsetId), i == n - 1 ? ";" : ",", comment);
				i++;
			}
		}
	}
}
