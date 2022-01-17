package org.sqlbuilder.pb;

import org.sqlbuilder.common.Insertable;

import java.util.Comparator;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

public class PbArg implements Insertable<PbArg>, Comparable<PbArg>
{
	public static final SortedSet<PbArg> SET = new TreeSet<>();

	public static final SortedSet<String> nSet = new TreeSet<>();

	public static final Properties nNames = new Properties();

	static
	{
		PbArg.nNames.setProperty("M", "modifier");
		PbArg.nNames.setProperty("A", "agent");
		PbArg.nNames.setProperty("@", "?");
	}

	private final PbExample example;

	private final String text;

	private final String n;

	private final PbFunc f;

	// C O N S T R U C T O R

	public PbArg(final PbExample example, final String text, final String n, final String f)
	{
		this.example = example;
		this.text = PbNormalizer.normalize(text);
		this.n = n;
		this.f = f == null ? null : new PbFunc(f.toLowerCase());
		SET.add(this);
		if (this.n != null && !this.n.isEmpty())
		{
			PbArg.nSet.add(this.n);
		}
	}

	public static PbArg make(final PbExample example, final String text, final String n, final String f)
	{
		return new PbArg(example, text, n, f);
	}

	// A C C E S S

	public PbExample getExample()
	{
		return this.example;
	}

	public String getText()
	{
		return this.text;
	}

	public PbFunc getF()
	{
		return this.f;
	}

	public String getN()
	{
		return this.n;
	}

	// O R D E R

	private static final Comparator<PbArg> COMPARATOR = Comparator //
			.comparing(PbArg::getExample) //
			.thenComparing(PbArg::getText) //
			.thenComparing(PbArg::getN) //
			.thenComparing(PbArg::getF);

	@Override
	public int compareTo(final PbArg that)
	{
		return COMPARATOR.compare(this, that);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("arg %s[%s][%s]", this.example, this.n, this.f);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		//final Long fId = PbFunc.funcMap.get(this.f);
		//final String nId = PbIdSet.toElement(this.n);

		// argid,text,n,f,exampleid
		// Long(1, this.id);
		// String(2, this.text);
		// Null(3, Types.INTEGER);
		// String(3, nId);
		// Null(4, Types.INTEGER);
		// Long(4, fId);
		// Long(5, this.example.getId());
		return null;
	}
}
