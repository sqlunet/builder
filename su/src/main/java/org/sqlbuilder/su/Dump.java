package org.sqlbuilder.su;

import com.articulate.sigma.Formula;
import com.articulate.sigma.KB;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

public class Dump
{
	public static void dumpTerms(final KB kb, final PrintStream ps)
	{
		int i = 0;
		for (final String term : kb.terms)
		{
			i++;
			ps.print("term " + i + "=" + term);
			ps.println(" doc=" + Dump.getDoc(kb, term));

			Dump.dumpParents(kb, term, ps);
			Dump.dumpChildren(kb, term, ps);
		}
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
				ps.print("\tparent" + i + "=" + formulaString);
				ps.println(" doc=" + Dump.getDoc(kb, formulaString));
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
				ps.print("\tchild" + i + "=" + formulaString);
				ps.println(" doc=" + Dump.getDoc(kb, formulaString));
			}
		}
	}

	public static void dumpFormulas(final KB kb, final PrintStream ps)
	{
		int i = 0;
		for (final Formula formula : kb.formulas.values())
		{
			i++;
			ps.println(i + " " + formula);
		}
	}

	public static void dumpPredicates(final KB kb, final PrintStream ps)
	{
		final Collection<String> predicates = kb.collectPredicates();
		int i = 0;
		for (final String predicate : predicates)
		{
			i++;
			ps.println(i + " " + predicate);
		}
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
