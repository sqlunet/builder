package org.sqlbuilder.pb.objects;

import org.sqlbuilder.annotations.RequiresIdFrom;
import org.sqlbuilder.common.*;
import org.sqlbuilder.pb.PbNormalizer;

import java.util.Comparator;

public class Arg implements HasId, Insertable, Comparable<Arg>
{
	private static final Comparator<Arg> COMPARATOR = Comparator //
			.comparing(Arg::getExample) //
			.thenComparing(Arg::getText) //
			.thenComparing(Arg::getN) //
			.thenComparing(Arg::getF, Comparator.nullsFirst(Comparator.naturalOrder()));

	public static final SetCollector<Arg> COLLECTOR = new SetCollector<>(COMPARATOR);

	private final Example example;

	private final String text;

	@NotNull
	private final ArgType n;

	@Nullable
	private final Func f;

	// C O N S T R U C T O R

	private Arg(final Example example, final String text, final String n, final String f)
	{
		assert n != null && !n.isEmpty();
		this.example = example;
		this.text = PbNormalizer.normalize(text);
		this.n = ArgType.make(n);
		this.f = f == null || f.isEmpty() ? null : Func.make(f.toLowerCase());
	}

	public static Arg make(final Example example, final String text, @NotNull final String n, final String f)
	{
		var a = new Arg(example, text, n, f);
		COLLECTOR.add(a);
		return a;
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

	@Nullable
	public Func getF()
	{
		return this.f;
	}

	@NotNull
	public ArgType getN()
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
	public int compareTo(@NotNull final Arg that)
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

	@RequiresIdFrom(type = Func.class)
	@RequiresIdFrom(type = Example.class)
	@Override
	public String dataRow()
	{
		// (argid),text,n,f,exampleid
		return String.format("'%s','%s',%s,%s", //
				Utils.escape(text), //
				n.getArgType(), //
				Func.getIntId(f), //
				example.getIntId());
	}

	@Override
	public String comment()
	{
		return String.format("%s,%s", n.getArgType(), f == null ? null : f.getFunc());
	}
}
