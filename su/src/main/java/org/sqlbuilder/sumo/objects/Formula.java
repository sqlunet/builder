package org.sqlbuilder.sumo.objects;

import com.articulate.sigma.NotNull;

import org.sqlbuilder.common.*;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

public class Formula implements HasId, Insertable, Serializable, Comparable<Formula>, Resolvable<String, Integer>
{
	public static final Comparator<Formula> COMPARATOR = Comparator.comparing(Formula::getFormulaText);

	public static final SetCollector<Formula> COLLECTOR = new SetCollector<>(COMPARATOR);

	public final com.articulate.sigma.Formula formula;

	public final SUFile file;

	// C O N S T R U C T

	private Formula(final com.articulate.sigma.Formula formula, final SUFile file)
	{
		this.formula = formula;
		this.file = file;
	}

	public static Formula make(final com.articulate.sigma.Formula formula)
	{
		final String filename = formula.getSourceFile();
		final Formula f = new Formula(formula, SUFile.make(filename));
		COLLECTOR.add(f);
		return f;
	}

	// A C C E S S

	public com.articulate.sigma.Formula getFormula()
	{
		return formula;
	}

	public String getFormulaText()
	{
		return formula.form;
	}

	public String getFile()
	{
		return file.getFilename();
	}

	// I D E N T I T Y

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		Formula that = (Formula) o;
		return formula.form.equals(that.formula.form);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(formula);
	}

	// O R D E R

	@Override
	public int compareTo(@NotNull final Formula that)
	{
		return COMPARATOR.compare(this, that);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return this.formula.form;
	}

	public String toShortString(final int ellipsizeAfter)
	{
		if (this.formula.form.length() > ellipsizeAfter)
		{
			return this.formula.form.substring(0, ellipsizeAfter) + "...";
		}
		return this.formula.form;
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("%d,%s,%s,%d", //
				resolve(), // id 1
				Utils.quotedEscapedString(toString()), // 2
				Utils.quotedEscapedString(file.filename), // 3
				resolveFile(file) // 4
		);
	}

	// R E S O L V E

	public int resolve()
	{
		return getIntId();
	}

	protected int resolveFile(final SUFile file)
	{
		return file.resolve();
	}

	@Override
	public Integer getIntId()
	{
		return COLLECTOR.get(this);
	}

	@Override
	public String resolving()
	{
		return formula.form;
	}
}
