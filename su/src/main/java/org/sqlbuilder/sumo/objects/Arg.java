package org.sqlbuilder.sumo.objects;

import org.sqlbuilder.common.Insertable;

import java.io.Serializable;

/**
 * This class encapsulates what relates a token in a logical statement to the entire statement. The type is arg when the term is nested only within one pair of
 * parentheses. The other possible types are "ant" for rule antecedent, "cons" for rule consequent, and "stmt" for cases where the term is nested inside
 * multiple levels of parentheses. argumentNum is only meaningful when the type is "arg"
 */
public class Arg implements Insertable, Serializable
{
	final boolean isInAntecedent;

	final boolean isInConsequent;

	public final boolean isArg;

	final boolean isStatement;

	public final int argumentNum;

	/**
	 * Constructor
	 *
	 * @param inAntecedent - whether the term appears in the antecedent of a rule.
	 * @param inConsequent - whether the term appears in the consequent of a rule.
	 * @param argumentNum  - the argument position in which the term appears. The predicate position is argument 0. The first argument is 1 etc.
	 * @param parenLevel   - if the parenthesis level is &gt; 1 then the term appears nested in a statement and the argument number is ignored.
	 */
	public Arg(final boolean inAntecedent, final boolean inConsequent, final int argumentNum, final int parenLevel)
	{
		this.isInAntecedent = inAntecedent;
		this.isInConsequent = inConsequent;
		this.isArg = !inAntecedent && !inConsequent && parenLevel == 1;
		this.argumentNum = this.isArg ? argumentNum : -1;
		this.isStatement = !inAntecedent && !inConsequent && parenLevel > 1;
	}

	/**
	 * Check
	 *
	 * @throws IllegalArgumentException illegal argument
	 */
	public void check()
	{
		if (this.isInAntecedent)
		{
			return;
		}
		else if (this.isInConsequent)
		{
			return;
		}
		else if (this.isArg)
		{
			return;
		}
		else if (this.isStatement)
		{
			return;
		}
		throw new IllegalArgumentException(toString());
	}

	/**
	 * Get type
	 *
	 * @return type
	 * @throws IllegalArgumentException illegal argument
	 */
	public String getType()
	{
		if (this.isInAntecedent)
		{
			return "p";
		}
		else if (this.isInConsequent)
		{
			return "c";
		}
		else if (this.isArg)
		{
			return "a";
		}
		else if (this.isStatement)
		{
			return "s";
		}
		throw new IllegalArgumentException(toString());
	}

	@Override
	public String toString()
	{
		if (this.isInAntecedent)
		{
			return "ant";
		}
		else if (this.isInConsequent)
		{
			return "cons";
		}
		else if (this.isArg)
		{
			return "arg-" + this.argumentNum;
		}
		else if (this.isStatement)
		{
			return "stmt";
		}
		return "ILLEGAL";
	}

	// I N S E R T
	@Override
	public String dataRow()
	{
		return String.format("%s,'%s'", //
				isArg ? argumentNum : "NULL", // 1
				getType() // 2
		);
	}

	@Override
	public String comment()
	{
		return String.format("%s", this);
	}
}
