package org.sqlbuilder.pb.objects;

import org.sqlbuilder.common.HasId;
import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.SetCollector;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.pb.PbIdSet;
import org.sqlbuilder.pb.PbNormalizer;

import java.util.*;

public class Arg implements HasId, Insertable, Comparable<Arg>
{
	private static final Comparator<Arg> COMPARATOR = Comparator //
			.comparing(Arg::getExample) //
			.thenComparing(Arg::getText) //
			.thenComparing(Arg::getN) //
			.thenComparing(Arg::getF);

	public static final SetCollector<Arg> COLLECTOR = new SetCollector<>(COMPARATOR);

	public static final SetCollector<String> N_COLLECTOR = new SetCollector<>(CharSequence::compare);

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

	public static Arg make(final Example example, final String text, final String n, final String f)
	{
		var a =  new Arg(example, text, n, f);
		if (a.n != null)
		{
			N_COLLECTOR.add(a.n);
		}
		COLLECTOR.add(a);
		return a;
	}

	private Arg(final Example example, final String text, final String n, final String f)
	{
		this.example = example;
		this.text = PbNormalizer.normalize(text);
		this.n = n != null && n.isEmpty() ? null : n;
		this.f = f == null ? null : Func.make(f.toLowerCase());
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

	@Override
	public Integer getIntId()
	{
		return COLLECTOR.get(this);
	}

	// O R D E R

	@Override
	public int compareTo(final Arg that)
	{
		return COMPARATOR.compare(this, that);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("arg %s[%s][%s]", example, n, f);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		final String nId = PbIdSet.toElement(n);

		// argid,text,n,f,exampleid
		// Long(1, this.id);
		// String(2, this.text);
		// Null(3, Types.INTEGER);
		// String(3, nId);
		// Null(4, Types.INTEGER);
		// Long(4, fId);
		// Long(5, this.example.getId());
		return String.format("'%s',%s,%s,%s", //
				Utils.escape(text), //
				nId,
				f.getIntId(),
				example.getIntId()
		);
	}

	@Override
	public String comment()
	{
		return String.format("%s,%s", n, f.getFunc());
	}
}
