package org.sqlbuilder.su;

import com.articulate.sigma.Formula;
import com.articulate.sigma.KB;

import java.io.PrintStream;
import java.util.Collection;

public class Dump
{
	private static final String UP = "\uD83E\uDC45";

	private static final String DOWN = "\uD83E\uDC47";

	public static void dumpTerms(final KB kb, final PrintStream ps)
	{
		for (final String term : kb.terms)
		{
			var doc = getDoc(kb, term);
			ps.printf("%s %s%n", term, doc == null ? "" : doc);

			Dump.dumpParents(kb, term, ps);
			Dump.dumpChildren(kb, term, ps);
		}
		ps.println(kb.terms.size());
	}

	public static void dumpParents(final KB kb, final String term, final PrintStream ps)
	{
		final Collection<Formula> formulas = kb.askWithRestriction(0, "subclass", 1, term);
		if (formulas != null && !formulas.isEmpty())
		{
			int i = 0;
			for (final Formula formula : formulas)
			{
				i++;
				final String formulaString = formula.getArgument(2);
				ps.println("\t" + UP + " " + i + " " + formulaString);
			}
		}
	}

	public static void dumpChildren(final KB kb, final String term, final PrintStream ps)
	{
		final Collection<Formula> formulas = kb.askWithRestriction(0, "subclass", 2, term);
		if (formulas != null && !formulas.isEmpty())
		{
			int i = 0;
			for (final Formula formula : formulas)
			{
				i++;
				final String formulaString = formula.getArgument(1);
				ps.println("\t"+ DOWN + " " + i + " " + formulaString);
			}
		}
	}

	public static void dumpFormulas(final KB kb, final PrintStream ps)
	{
		Collection<Formula> formulas = kb.formulas.values();
		formulas.stream().sorted().forEach(ps::println);
		ps.println(formulas.size());
	}

	public static void dumpFunctions(final KB kb, final PrintStream ps)
	{
		Collection<String> functions = kb.collectFunctions();
		functions.stream().sorted().forEach(ps::println);
		ps.println(functions.size());
	}

	public static void dumpRelations(final KB kb, final PrintStream ps)
	{
		Collection<String> relations = kb.collectRelations();
		relations.stream().sorted().forEach(ps::println);
		ps.println(relations.size());
	}

	public static void dumpPredicates(final KB kb, final PrintStream ps)
	{
		Collection<String> predicates = kb.collectPredicates();
		predicates.stream().sorted().forEach(ps::println);
		ps.println(predicates.size());
	}

	private static String getDoc(final KB kb, final String term)
	{
		final Collection<Formula> formulas = kb.askWithRestriction(0, "documentation", 1, term);
		if (formulas != null && !formulas.isEmpty())
		{
			final Formula formula = formulas.iterator().next();
			String doc = formula.getArgument(3);
			// doc = kb.formatDocumentation("http://", doc);
			doc = doc.replaceAll("\\n", "");
			return doc;
		}
		return null;
	}
}
