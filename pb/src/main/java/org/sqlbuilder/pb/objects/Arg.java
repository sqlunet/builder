package org.sqlbuilder.pb.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.pb.PbNormalizer;

import java.util.*;

public class Arg implements Insertable, Comparable<Arg>
{
	public static final Set<Arg> SET = new HashSet<>();

	public static final Set<String> nSet = new HashSet<>();

	public static final Properties nNames = new Properties();

	static
	{
		Arg.nNames.setProperty("M", "modifier");
		Arg.nNames.setProperty("A", "agent");
		Arg.nNames.setProperty("@", "?");
	}

	private final Example example;

	private final String text;

	private final String n;

	private final Func f;

	// C O N S T R U C T O R

	public Arg(final Example example, final String text, final String n, final String f)
	{
		this.example = example;
		this.text = PbNormalizer.normalize(text);
		this.n = n;
		this.f = f == null ? null : new Func(f.toLowerCase());
		SET.add(this);
		if (this.n != null && !this.n.isEmpty())
		{
			Arg.nSet.add(this.n);
		}
	}

	public static Arg make(final Example example, final String text, final String n, final String f)
	{
		return new Arg(example, text, n, f);
	}

	// A C C E S S

	public Example getExample()
	{
		return this.example;
	}

	public String getText()
	{
		return this.text;
	}

	public Func getF()
	{
		return this.f;
	}

	public String getN()
	{
		return this.n;
	}

	// O R D E R

	private static final Comparator<Arg> COMPARATOR = Comparator //
			.comparing(Arg::getExample) //
			.thenComparing(Arg::getText) //
			.thenComparing(Arg::getN) //
			.thenComparing(Arg::getF);

	@Override
	public int compareTo(final Arg that)
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
