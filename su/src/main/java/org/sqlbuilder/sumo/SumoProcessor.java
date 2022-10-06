package org.sqlbuilder.sumo;

import org.sqlbuilder.common.*;
import org.sqlbuilder.sumo.joins.Formula_Arg;
import org.sqlbuilder.sumo.joins.Term_Synset;
import org.sqlbuilder.sumo.objects.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class SumoProcessor extends Processor
{
	private static final String[] POSES = {"noun", "verb", "adj", "adv"};

	public static final String SUMO_TEMPLATE = "WordNetMappings/WordNetMappings30-%s.txt";

	protected final File inDir;

	protected final Names names;

	protected String header;

	protected String columns;

	protected boolean resolve;

	protected File outDir;

	protected final Properties conf;

	public SumoProcessor(final Properties conf)
	{
		super("sumo");
		this.resolve = false;
		this.names = new Names("su");
		this.conf = conf;
		this.header = conf.getProperty("su_header");
		this.columns = names.columns("terms_synsets");
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
		for (final com.articulate.sigma.Formula formula : kb.formulaMap.values())
		{
			Formula.make(formula);
		}
	}

	public static void collectSynsets(final String fileTemplate, final PrintStream pse) throws IOException
	{
		for (final String posName : POSES)
		{
			final String filename = String.format(fileTemplate, posName);
			final char pos = posName.charAt(0);
			collectSynsets(pos, filename, pse);
		}
	}

	public static void collectSynsets(final char pos, final String filename, final PrintStream pse) throws IOException
	{
		// iterate on synsets
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(filename)))))
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
					Term_Synset.parse(term, line, pos); // side effect: term mapping collected into set
				}
				catch (IllegalArgumentException iae)
				{
					pse.println("line " + lineno + '-' + pos + " " + ": ILLEGAL [" + iae.getMessage() + "] : " + line);
				}
				catch (AlreadyFoundException afe)
				{
					pse.println("line " + lineno + '-' + pos + " " + ": DUPLICATE [" + afe.getMessage() + "] : " + line);
				}
			}
		}
	}

	// I N S E R T

	public static void insertFiles(final PrintStream ps, final Collection<SUFile> files, final String table, final String columns)
	{
		int n = files.size();
		if (n > 0)
		{
			int i = 0;
			ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
			for (final SUFile file : files)
			{
				String row = file.dataRow();
				ps.printf("(%s)%s%n", row, i == n - 1 ? ";" : ",");
				i++;
			}
		}
	}

	public static void insertTerms(final PrintStream ps, final Collection<Term> terms, final String table, final String columns)
	{
		int n = terms.size();
		if (n > 0)
		{
			int i = 0;
			ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
			for (final Term term : terms)
			{
				String row = term.dataRow();
				ps.printf("(%s)%s%n", row, i == n - 1 ? ";" : ",");
				i++;
			}
		}
	}

	public static void insertTermsAndAttrs(final PrintStream ps, final PrintStream ps2, final Collection<Term> terms, final Kb kb, final String table, final String columns, final String table2, final String columns2)
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
				ps.printf("(%s)%s%n", row, i == n - 1 ? ";" : ",");

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

	public static void insertTermAttrs(final PrintStream ps, final Collection<Term> terms, final Kb kb, final String table, final String columns)
	{
		int n = terms.size();
		if (n > 0)
		{
			int j = 0;
			ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
			for (final Term term : terms)
			{
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

	public static void insertFormulas(final PrintStream ps, final Collection<Formula> formulas, final String table, final String columns)
	{
		int n = formulas.size();
		if (n > 0)
		{
			int i = 0;
			ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
			for (final Formula formula : formulas)
			{
				// formula
				String row = formula.dataRow();
				ps.printf("(%s)%s%n", row, i == n - 1 ? ";" : ",");
				i++;
			}
		}
	}

	public static void insertFormulasAndArgs(final PrintStream ps, final PrintStream ps2, final Collection<Formula> formulas, final String table, final String columns, final String table2, final String columns2) throws NotFoundException, ParseException, IOException
	{
		int n = formulas.size();
		if (n > 0)
		{
			int i = 0;
			int j = 0;
			ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
			ps2.printf("INSERT INTO %s (%s) VALUES%n", table2, columns2);
			for (final Formula formula : formulas)
			{
				// formula
				String row = formula.dataRow();
				ps.printf("(%s)%s%n", row, i == n - 1 ? ";" : ",");

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
					ps2.printf("%s(%s) /* %s, %s */", j == 0 ? "" : ",\n", row2, commentArg2, /* commentTerm2,*/ commentFormArg2);
					j++;
				}
				i++;
			}
			ps2.println(";");
		}
	}

	public static void insertFormulaArgs(final PrintStream ps, final Collection<Formula> formulas, final String table, final String columns) throws ParseException, IOException
	{
		int n = formulas.size();
		if (n > 0)
		{
			int j = 0;
			ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
			for (final Formula formula : formulas)
			{
				// formula args
				List<Formula_Arg> formulas_args = Formula_Arg.make(formula);
				for (final Formula_Arg formula_arg : formulas_args)
				{
					String row2 = formula_arg.dataRow();
					String comment2 = formula_arg.comment();
					ps.printf("%s(%s) /* %s */", j == 0 ? "" : ",\n", row2, comment2);
					j++;
				}
			}
			ps.println(";");
		}
	}

	public void processSynsets(final PrintStream ps, final Collection<Term_Synset> terms_synsets, final String table, final String columns)
	{
		insertSynsets(ps, terms_synsets, table, columns);
	}

	public static void insertSynsets(final PrintStream ps, final Collection<Term_Synset> terms_synsets, final String table, final String columns)
	{
		int n = terms_synsets.size();
		if (n > 0)
		{
			int i = 0;
			ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
			for (final Term_Synset map : terms_synsets)
			{
				String row = map.dataRow();
				String comment = map.comment();
				ps.printf("(%s)%s -- %s%n", row, i == n - 1 ? ";" : ",", comment);
				i++;
			}
		}
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
				insertFiles(ps, SUFile.COLLECTOR.keySet(), names.table("files"), names.columns("files"));
			}

			// terms + attrs
			try ( //
			      PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("terms"))), true, StandardCharsets.UTF_8); //
			      PrintStream ps2 = new PrintStream(new FileOutputStream(new File(outDir, names.file("terms_attrs"))), true, StandardCharsets.UTF_8) //
			)
			{
				ps.println("-- " + header);
				ps2.println("-- " + header);
				insertTermsAndAttrs(ps, ps2, Term.COLLECTOR.keySet(), KBLoader.kb, names.table("terms"), names.columns("terms"), names.table("terms_attrs"), names.columns("terms_attrs"));
			}

			// formulas + args
			try ( //
			      PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("formulas"))), true, StandardCharsets.UTF_8); //
			      PrintStream ps2 = new PrintStream(new FileOutputStream(new File(outDir, names.file("formulas_args"))), true, StandardCharsets.UTF_8) //
			)
			{
				ps.println("-- " + header);
				ps2.println("-- " + header);
				insertFormulasAndArgs(ps, ps2, Formula.COLLECTOR.keySet(), names.table("formulas"), names.columns("formulas"), names.table("formulas_args"), names.columns("formulas_args"));
			}

			// terms_synsets
			try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, names.file("terms_synsets"))), true, StandardCharsets.UTF_8))
			{
				ps.println("-- " + header);
				processSynsets(ps, Term_Synset.SET, names.table("terms_synsets"), columns);
			}
		}
		catch (NotFoundException | ParseException e)
		{
			throw new RuntimeException(e);
		}
	}
}
