package org.sqlbuilder.su;

import org.sqlbuilder.common.AlreadyFoundException;
import org.sqlbuilder.common.Names;
import org.sqlbuilder.common.NotFoundException;
import org.sqlbuilder.common.Processor;
import org.sqlbuilder.su.joins.Formula_Arg;
import org.sqlbuilder.su.joins.Term_Synset;
import org.sqlbuilder.su.objects.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class SuProcessor extends Processor
{
	private static final String[] POSES = {"noun", "verb", "adj", "adv"};

	public static final String SUMO_TEMPLATE = "WordNetMappings/WordNetMappings30-%s.txt";

	protected final File inDir;

	protected final Names names;

	protected String header;

	protected String termsColumns;

	protected String synsetsColumns;

	protected boolean resolve;

	protected File outDir;

	protected final Properties conf;

	public SuProcessor(final Properties conf)
	{
		super("sumo");
		this.resolve = false;
		this.names = new Names("su");
		this.conf = conf;
		this.header = conf.getProperty("su_header");
		this.termsColumns = names.columns("terms");
		this.synsetsColumns = names.columns("terms_synsets");
		this.inDir = new File(conf.getProperty("su_home", System.getenv().get("SUMOHOME")));
		this.outDir = new File(conf.getProperty("su_outdir", "sql/data"));
		if (!this.outDir.exists())
		{
			//noinspection ResultOfMethodCallIgnored
			this.outDir.mkdirs();
		}
	}

	// C O L L E C T

	public static void collectFiles(final Kb kb)
	{
		for (final String filename : kb.getFilenames())
		{
			SUFile.make(filename);
		}
	}

	public static void collectTerms(final Kb kb)
	{
		for (final String term : kb.terms)
		{
			Term.make(term);
		}
	}

	public static void collectFormulas(final Kb kb)
	{
		for (final com.articulate.sigma.Formula formula : kb.formulas.values())
		{
			Formula.make(formula);
		}
	}

	public static void collectSynsets(final String fileTemplate, final PrintStream pse) throws IOException
	{
		for (final String posName : POSES)
		{
			final String filename = String.format(fileTemplate, posName);
			collectFileSynsets(filename, pse);
		}
	}

	public static void collectFileSynsets(final String filename, final PrintStream pse) throws IOException
	{
		// iterate on synsets
		final Path path = Paths.get(filename);
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(path))))
		{
			int lineno = 0;
			String line;
			while ((line = reader.readLine()) != null)
			{
				lineno++;
				line = line.trim();
				if (line.isEmpty() || line.charAt(0) == ' ' || line.charAt(0) == ';' || !line.contains("&%"))
				{
					continue;
				}

				// read
				try
				{
					final String term = Term.parse(line);
					/* final Term_Sense mapping = */
					Term_Synset.parse(term, line); // side effect: term mapping collected into set
				}
				catch (IllegalArgumentException iae)
				{
					pse.println(path.getFileName().toString() + ':' + lineno + " " + ": ILLEGAL [" + iae.getMessage() + "] : " + line);
				}
				catch (AlreadyFoundException afe)
				{
					pse.println(path.getFileName().toString() + ':' + lineno + " " + ": DUPLICATE [" + afe.getMessage() + "] : " + line);
				}
			}
		}
	}

	// I N S E R T

	public static void insertFiles(final PrintStream ps, final Iterable<SUFile> files, final String table, final String columns)
	{
		Iterator<SUFile> iterator = files.iterator();
		if (iterator.hasNext())
		{
			ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
			while (iterator.hasNext())
			{
				final SUFile file = iterator.next();
				boolean isLast = !iterator.hasNext();
				String row = file.dataRow();
				ps.printf("(%s)%s%n", row, isLast ? ";" : ",");
			}
		}
	}

	public static void insertTerms(final PrintStream ps, final Iterable<Term> terms, final String table, final String columns)
	{
		Iterator<Term> iterator = terms.iterator();
		if (iterator.hasNext())
		{
			ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
			while (iterator.hasNext())
			{
				final Term term = iterator.next();
				boolean isLast = !iterator.hasNext();
				String row = term.dataRow();
				ps.printf("(%s)%s%n", row, isLast ? ";" : ",");
			}
		}
	}

	public static void insertTermsAndAttrs(final PrintStream ps, final PrintStream ps2, final Iterable<Term> terms, final Kb kb, final String table, final String columns, final String table2, final String columns2)
	{
		Iterator<Term> iterator = terms.iterator();
		if (iterator.hasNext())
		{
			int i = 0;
			ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
			ps2.printf("INSERT INTO %s (%s) VALUES%n", table2, columns2);
			while (iterator.hasNext())
			{
				final Term term = iterator.next();
				boolean isLast = !iterator.hasNext();
				String row = term.dataRow();
				ps.printf("(%s)%s%n", row, isLast ? ";" : ",");

				int termid = term.resolve();
				try
				{
					final Collection<TermAttr> attributes = TermAttr.make(term, kb);
					for (final TermAttr attribute : attributes)
					{
						String row2 = String.format("%d,%s", termid, attribute.dataRow());
						String comment2 = term.comment();
						ps2.printf("%s(%s) /* %s */", i == 0 ? "" : ",\n", row2, comment2);
						i++;
					}
				}
				catch (NotFoundException ignored)
				{
				}
			}
			ps2.println(";");
		}
	}

	public static void insertTermAttrs(final PrintStream ps, final Iterable<Term> terms, final Kb kb, final String table, final String columns)
	{
		Iterator<Term> iterator = terms.iterator();
		if (iterator.hasNext())
		{
			int j = 0;
			ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
			while (iterator.hasNext())
			{
				final Term term = iterator.next();
				int termid = term.resolve();
				try
				{
					final Collection<TermAttr> attributes = TermAttr.make(term, kb);
					for (final TermAttr attribute : attributes)
					{
						String row2 = String.format("%d,%s", termid, attribute.dataRow());
						String comment2 = term.comment();
						ps.printf("%s(%s) /* %s */", j == 0 ? "" : ",\n", row2, comment2);
						j++;
					}
				}
				catch (NotFoundException ignored)
				{
				}
			}
			ps.println(";");
		}
	}

	public static void insertFormulas(final PrintStream ps, final Iterable<Formula> formulas, final String table, final String columns)
	{
		Iterator<Formula> iterator = formulas.iterator();
		if (iterator.hasNext())
		{
			ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
			while (iterator.hasNext())
			{
				final Formula formula = iterator.next();
				boolean isLast = !iterator.hasNext();
				String row = formula.dataRow();
				ps.printf("(%s)%s%n", row, isLast ? ";" : ",");
			}
		}
	}

	public static void insertFormulasAndArgs(final PrintStream ps, final PrintStream ps2, final Iterable<Formula> formulas, final String table, final String columns, final String table2, final String columns2) throws NotFoundException, ParseException, IOException
	{
		Iterator<Formula> iterator = formulas.iterator();
		if (iterator.hasNext())
		{
			int i = 0;
			ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
			ps2.printf("INSERT INTO %s (%s) VALUES%n", table2, columns2);
			while (iterator.hasNext())
			{
				final Formula formula = iterator.next();
				boolean isLast = !iterator.hasNext();

				// formula
				String row = formula.dataRow();
				ps.printf("(%s)%s%n", row, isLast ? ";" : ",");

				// formula args
				List<Formula_Arg> formulas_args = Formula_Arg.make(formula);
				for (final Formula_Arg formula_arg : formulas_args)
				{
					String row2 = formula_arg.dataRow();
					Arg arg = formula_arg.getArg();
					String commentArg2 = arg.comment();
					//Term term = formula_arg.getTerm();
					//String commentTerm2 = term.comment();
					String commentFormArg2 = formula_arg.comment();
					ps2.printf("%s(%s) /* %s, %s */", i == 0 ? "" : ",\n", row2, commentArg2, /* commentTerm2,*/ commentFormArg2);
					i++;
				}
			}
			ps2.println(";");
		}
	}

	public static void insertFormulaArgs(final PrintStream ps, final Iterable<Formula> formulas, final String table, final String columns) throws ParseException, IOException
	{
		Iterator<Formula> iterator = formulas.iterator();
		if (iterator.hasNext())
		{
			int i = 0;
			ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
			while (iterator.hasNext())
			{
				final Formula formula = iterator.next();

				// formula args
				List<Formula_Arg> formulas_args = Formula_Arg.make(formula);
				for (final Formula_Arg formula_arg : formulas_args)
				{
					String row2 = formula_arg.dataRow();
					String comment2 = formula_arg.comment();
					ps.printf("%s(%s) /* %s */", i == 0 ? "" : ",\n", row2, comment2);
					i++;
				}
			}
			ps.println(";");
		}
	}

	public static void insertSynsets(final PrintStream ps, final Iterable<Term_Synset> terms_synsets, final String table, final String columns)
	{
		Iterator<Term_Synset> iterator = terms_synsets.iterator();
		if (iterator.hasNext())
		{
			ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
			while (iterator.hasNext())
			{
				final Term_Synset map = iterator.next();
				boolean isLast = !iterator.hasNext();

				String row = map.dataRow();
				String comment = map.comment();
				ps.printf("(%s)%s -- %s%n", row, isLast ? ";" : ",", comment);
			}
		}
	}

	public void processTerms(final PrintStream ps, final Iterable<Term> terms, final String table, final String columns)
	{
		insertTerms(ps, terms, table, columns);
	}

	public void processTermsAndAttrs(final PrintStream ps, final PrintStream ps2, final Iterable<Term> terms, final Kb kb, final String table, final String columns, final String table2, final String columns2)
	{
		insertTermsAndAttrs(ps, ps2, terms, kb, table, columns, table2, columns2);
	}

	public void processSynsets(final PrintStream ps, final Iterable<Term_Synset> terms_synsets, final String table, final String columns)
	{
		insertSynsets(ps, terms_synsets, table, columns);
	}

	// R U N

	@Override
	public void run() throws IOException
	{
		new KBLoader().load();
		collectFiles(KBLoader.kb);
		collectTerms(KBLoader.kb);
		collectFormulas(KBLoader.kb);
		collectSynsets(this.inDir + File.separator + SUMO_TEMPLATE, System.err);

		try ( //
		      var ignored1 = SUFile.COLLECTOR.open(); //
		      var ignored2 = Term.COLLECTOR.open(); //
		      var ignored3 = Formula.COLLECTOR.open() //
		) //
		{
			// files
			try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("files"))), true, StandardCharsets.UTF_8))
			{
				ps.println("-- " + header);
				insertFiles(ps, SUFile.COLLECTOR, names.table("files"), names.columns("files"));
			}

			// terms + attrs
			try ( //
			      PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("terms"))), true, StandardCharsets.UTF_8); //
			      PrintStream ps2 = new PrintStream(new FileOutputStream(new File(outDir, names.file("terms_attrs"))), true, StandardCharsets.UTF_8) //
			)
			{
				ps.println("-- " + header);
				ps2.println("-- " + header);
				processTermsAndAttrs(ps, ps2, Term.COLLECTOR, KBLoader.kb, names.table("terms"), termsColumns, names.table("terms_attrs"), names.columns("terms_attrs"));
			}

			// formulas + args
			try ( //
			      PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("formulas"))), true, StandardCharsets.UTF_8); //
			      PrintStream ps2 = new PrintStream(new FileOutputStream(new File(outDir, names.file("formulas_args"))), true, StandardCharsets.UTF_8) //
			)
			{
				ps.println("-- " + header);
				ps2.println("-- " + header);
				insertFormulasAndArgs(ps, ps2, Formula.COLLECTOR, names.table("formulas"), names.columns("formulas"), names.table("formulas_args"), names.columns("formulas_args"));
			}

			// terms_synsets
			try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("terms_synsets"))), true, StandardCharsets.UTF_8))
			{
				ps.println("-- " + header);
				processSynsets(ps, Term_Synset.SET, names.table("terms_synsets"), synsetsColumns);
			}
		}
		catch (NotFoundException | ParseException e)
		{
			throw new RuntimeException(e);
		}
	}
}
