package org.sqlbuilder.su.objects;

import com.articulate.sigma.Formula;

import org.sqlbuilder.common.NotFoundException;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.su.Kb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TermAttr
{
	public final Character attr;

	private static final Character ISFUNCTION = 'y';

	private static final Character ISMATHFUNCTION = 'm';

	private static final Character ISCOMPARISONOP = '~';

	private static final Character ISLOGICALOP = 'l';

	private static final Character ISQUANTIFIER = 'q';

	private static final Character SUBCLASSOFRELATION = 'R';

	private static final Character SUBCLASSOFFUNCTION = 'F';

	private static final Character SUBCLASSOFPREDICATE = 'P';

	private static final Character SUBCLASSOFATTRIBUTE = 'A';

	private static final Character CHILDOFRELATION = 'r';

	private static final Character CHILDOFFUNCTION = 'f';

	private static final Character CHILDOFPREDICATE = 'p';

	private static final Character CHILDOFATTRIBUTE = 'a';

	// C O N S T R U C T

	private TermAttr(final Character attr)
	{
		this.attr = attr;
	}

	public static Collection<TermAttr> make(final Term sumoTerm, final Kb kb) throws NotFoundException
	{
		String term = sumoTerm.getTerm();

		final List<TermAttr> result = new ArrayList<>();

		if (Formula.isFunction(term))
		{
			result.add(new TermAttr(TermAttr.ISFUNCTION));
		}
		if (Formula.isMathFunction(term))
		{
			result.add(new TermAttr(TermAttr.ISMATHFUNCTION));
		}
		if (Formula.isComparisonOperator(term))
		{
			result.add(new TermAttr(TermAttr.ISCOMPARISONOP));
		}
		if (Formula.isLogicalOperator(term))
		{
			result.add(new TermAttr(TermAttr.ISLOGICALOP));
		}
		if (Formula.isQuantifier(term))
		{
			result.add(new TermAttr(TermAttr.ISQUANTIFIER));
		}

		if (kb.childOf(term, "Relation"))
		{
			result.add(new TermAttr(TermAttr.CHILDOFRELATION));
		}
		if (kb.childOf(term, "Predicate"))
		{
			result.add(new TermAttr(TermAttr.CHILDOFPREDICATE));
		}
		if (kb.childOf(term, "Function"))
		{
			result.add(new TermAttr(TermAttr.CHILDOFFUNCTION));
		}
		if (kb.childOf(term, "Attribute"))
		{
			result.add(new TermAttr(TermAttr.CHILDOFATTRIBUTE));
		}

		if (kb.isSubclass(term, "Relation"))
		{
			result.add(new TermAttr(TermAttr.SUBCLASSOFRELATION));
		}
		if (kb.isSubclass(term, "Predicate"))
		{
			result.add(new TermAttr(TermAttr.SUBCLASSOFPREDICATE));
		}
		if (kb.isSubclass(term, "Function"))
		{
			result.add(new TermAttr(TermAttr.SUBCLASSOFFUNCTION));
		}
		if (kb.isSubclass(term, "Attribute"))
		{
			result.add(new TermAttr(TermAttr.SUBCLASSOFATTRIBUTE));
		}

		if (result.isEmpty())
			throw new NotFoundException(term);

		return result;
	}

	// A C C E S S

	public Character getAttr()
	{
		return attr;
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return attr.toString();
	}

	// I N S E R T

	public String dataRow()
	{
		return String.format("%s", //
				Utils.nullableQuotedEscapedString(attr.toString())); // 1
	}
}
