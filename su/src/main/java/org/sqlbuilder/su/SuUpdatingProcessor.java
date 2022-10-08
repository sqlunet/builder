package org.sqlbuilder.su;

import org.sqlbuilder.su.joins.Term_Synset;
import org.sqlbuilder.su.objects.Term;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Locale;
import java.util.Properties;

public class SuUpdatingProcessor extends SuResolvingProcessor
{
	public SuUpdatingProcessor(final Properties conf) throws IOException, ClassNotFoundException
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
			// terms
			try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("terms"))), true, StandardCharsets.UTF_8))
			{
				ps.println("-- " + header);
				processTerms(ps, Term.COLLECTOR.keySet(), names.table("terms"), termsColumns);
			}

			// terms_senses
			try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("terms_synsets"))), true, StandardCharsets.UTF_8))
			{
				ps.println("-- " + header);
				processSynsets(ps, Term_Synset.SET, names.table("terms_synsets"), synsetsColumns);
			}
		}
	}

	@Override
	public void processTerms(final PrintStream ps, final Collection<Term> terms, final String table, final String columns)
	{
		int n = terms.size();
		if (n > 0)
		{
			final String colWordId = names.column("terms.wordid");
			final String colTermId = names.column("terms.termid");

			int i = 0;
			for (final Term term : terms)
			{
				updateTermRow(ps, table, ++i, term, colWordId, colTermId);
			}
		}
	}

	@Override
	public void processTermsAndAttrs(final PrintStream ps, final PrintStream ps2, final Collection<Term> terms, final Kb kb, final String table, final String columns, final String table2, final String columns2)
	{
		processTerms(ps, terms, table, columns);
	}

	private void updateTermRow(final PrintStream ps, final String table, final Integer index, final Term term, final String... columns)
	{
		var resolvedWordId = wordResolver.apply(term.term.toLowerCase(Locale.ENGLISH));
		if (resolvedWordId != null)
		{
			var resolvedTermId = term.resolve();

			String setClause = String.format("%s=%s", columns[0], resolvedWordId);
			String whereClause = String.format("%s=%d", columns[1], resolvedTermId //
			);
			ps.printf("UPDATE %s SET %s WHERE %s; -- %d %s%n", table, setClause, whereClause, index + 1, term.comment());
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
				updateMapRow(ps, table, ++i, map, colSynsetId, colMapId, colPosId);
			}
		}
	}

	private void updateMapRow(final PrintStream ps, final String table, final Integer index, final Term_Synset map, final String... columns)
	{
		// 30 to 31
		char posId = map.posId; // {n,v,a,r}
		long synset30Id = map.synsetId;
		var synset31Id = synset31Resolver.apply(posId, synset30Id);
		if (synset31Id != null)
		{
			// 31 to XX
			var synsetId = String.format("%08d-%c", synset31Id, posId);
			Integer resolvedSynsetId = synsetResolver.apply(synsetId);
			if (resolvedSynsetId != null)
			{
				String setClause = String.format("%s=%d", columns[0], resolvedSynsetId);
				String whereClause = String.format("%s=%s AND %s='%s'", //
						columns[1], synsetId, //
						columns[2], posId //
				);
				ps.printf("UPDATE %s SET %s WHERE %s; -- %d %s%n", table, setClause, whereClause, index + 1, map.comment());
			}
		}
	}
}