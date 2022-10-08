package org.sqlbuilder.su;

import org.sqlbuilder.common.NotFoundException;
import org.sqlbuilder.su.joins.Term_Synset;
import org.sqlbuilder.su.objects.Term;
import org.sqlbuilder.su.objects.TermAttr;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Locale;
import java.util.Properties;

public class SuResolvingProcessor extends SuProcessor
{
	protected final SuWordResolver wordResolver;
	protected final SuSynsetResolver synsetResolver;

	protected final SuSynset31Resolver synset31Resolver;

	public SuResolvingProcessor(final Properties conf) throws IOException, ClassNotFoundException
	{
		super(conf);

		// header
		this.header += "\n-- " + conf.getProperty("wn_resolve_against");

		// columns
		this.termsColumns = names.columns("terms", true);
		this.synsetsColumns = names.columns("terms_synsets", true);

		// outdir
		this.outDir = new File(conf.getProperty("su_outdir_resolved", "sql/data_resolved"));
		if (!this.outDir.exists())
		{
			//noinspection ResultOfMethodCallIgnored
			this.outDir.mkdirs();
		}

		// resolver
		this.resolve = true;
		String wordSerFile = conf.getProperty("word_nids");
		this.wordResolver = new SuWordResolver(wordSerFile);
		String synsetSerFile = conf.getProperty("synset_nids");
		this.synsetResolver = new SuSynsetResolver(synsetSerFile);
		String synset31SerFile = conf.getProperty("synsets30_to_synsets31");
		this.synset31Resolver = new SuSynset31Resolver(synset31SerFile);
	}

	@Override
	public void processTerms(final PrintStream ps, final Collection<Term> terms, final String table, final String columns)
	{
		int n = terms.size();
		if (n > 0)
		{
			int i = 0;
			ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
			for (final Term term : terms)
			{
				String row = term.dataRow();
				Integer wordId = wordResolver.apply(term.term.toLowerCase(Locale.ENGLISH));
				ps.printf("(%s,%s)%s%n", row, Utils.nullableInt(wordId), i == n - 1 ? ";" : ",");
				i++;
			}
		}
	}

	@Override
	public void processTermsAndAttrs(final PrintStream ps, final PrintStream ps2, final Collection<Term> terms, final Kb kb, final String table, final String columns, final String table2, final String columns2)
	{
		int n = terms.size();
		if (n > 0)
		{
			int i = 0;
			int j = 0;
			ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
			ps2.printf("INSERT INTO %s (%s) VALUES%n", table2, columns2);
			for (final Term term : terms)
			{
				String row = term.dataRow();
				Integer wordId = wordResolver.apply(term.term.toLowerCase(Locale.ENGLISH));
				ps.printf("(%s,%s)%s%n", row, Utils.nullableInt(wordId), i == n - 1 ? ";" : ",");

				int termid = term.resolve();
				try
				{
					final Collection<TermAttr> attributes = TermAttr.make(term, kb);
					for (final TermAttr attribute : attributes)
					{
						String row2 = String.format("%d,%s", termid, attribute.dataRow());
						String comment2 = term.comment();
						ps2.printf("%s(%s) /* %s */", j == 0 ? "" : ",\n", row2, comment2);
						j++;
					}
				}
				catch (NotFoundException ignored)
				{
				}
				i++;
			}
			ps2.println(";");
		}
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

				// 30 to 31
				char posId = map.posId; // {n,v,a,r,s}
				long synset30Id = map.synsetId;
				Long synset31Id = synset31Resolver.apply(posId, synset30Id);

				// 31 to XX
				Integer resolvedSynsetId = null;
				if (synset31Id != null)
				{
					String synsetId = String.format("%08d-%c", synset31Id, posId);
					resolvedSynsetId = synsetResolver.apply(synsetId);
				}
				String comment = map.comment();
				ps.printf("(%s,%s)%s -- %s%n", row, Utils.nullableInt(resolvedSynsetId), i == n - 1 ? ";" : ",", comment);
				i++;
			}
		}
	}
}
