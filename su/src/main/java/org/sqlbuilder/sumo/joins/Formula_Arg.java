package org.sqlbuilder.sumo.joins;

import com.articulate.sigma.NotNull;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.NotFoundException;
import org.sqlbuilder.sumo.FormulaParser;
import org.sqlbuilder.sumo.objects.Arg;
import org.sqlbuilder.sumo.objects.Formula;
import org.sqlbuilder.sumo.objects.Term;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

public class Formula_Arg implements Insertable, Serializable, Comparable<Formula_Arg>
{
	private static final Comparator<Formula_Arg> COMPARATOR = Comparator.comparing(Formula_Arg::getArgNum);
	private final Formula formula;

	private final Term term;

	private final Arg arg;

	// C O N S T R U C T

	private Formula_Arg(final Formula formula, final Term term, final Arg arg)
	{
		this.formula = formula;
		this.term = term;
		this.arg = arg;
	}

	public static List<Formula_Arg> make(final Formula formula) throws IllegalArgumentException, ParseException, IOException
	{
		final List<Formula_Arg> result = new ArrayList<>();
		final Map<String, Arg> map = FormulaParser.parse(formula.formula);
		for (final Map.Entry<String, Arg> entry : map.entrySet())
		{
			final String key = entry.getKey();
			final Term term = Term.make(key);
			final Arg parse = entry.getValue();
			result.add(new Formula_Arg(formula, term, parse));
		}
		Collections.sort(result);
		return result;
	}

	// A C C E S S

	public Formula getFormula()
	{
		return formula;
	}

	public Term getTerm()
	{
		return term;
	}

	public Arg getArg()
	{
		return arg;
	}

	public int getArgNum()
	{
		return arg.argumentNum;
	}

	// O R D E R

	@Override
	public int compareTo(@NotNull final Formula_Arg that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T
	@Override
	public String dataRow()
	{
		return String.format("%s,%s,%s", //
				resolveFormula(formula), // 1
				resolveTerm(term), // 2
				arg.dataRow() // 3
		);
	}

	@Override
	public String comment()
	{
		return String.format("%s, %s", term.term, formula.toShortString(32));
	}

	// R E S O L V E

	protected int resolve()
	{
		return -1;
	}

	protected int resolveTerm(final Term term)
	{
		return term.resolve();
	}

	protected int resolveFormula(final Formula formula)
	{
		return formula.resolve();
	}
}
