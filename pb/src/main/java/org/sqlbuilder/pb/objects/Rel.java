package org.sqlbuilder.pb.objects;

import org.sqlbuilder.common.*;
import org.sqlbuilder.pb.PbNormalizer;

import java.util.Comparator;

public class Rel implements HasId, Insertable, Comparable<Rel>
{
	private static final Comparator<Rel> COMPARATOR = Comparator //
			.comparing(Rel::getExample) //
			.thenComparing(Rel::getText) //
			.thenComparing(Rel::getF, Comparator.nullsFirst(Comparator.naturalOrder()));

	public static final SetCollector<Rel> COLLECTOR = new SetCollector<>(COMPARATOR);

	private final String text;

	private final Func f;

	private final Example example;

	// C O N S T R U C T O R

	public static Rel make(final Example example, final String text, final Func f)
	{
		var r = new Rel(example, text, f);
		COLLECTOR.add(r);
		return r;
	}

	private Rel(final Example example, final String text, final Func f)
	{
		this.text = PbNormalizer.normalize(text);
		this.f = f;
		this.example = example;
	}

	// A C C E S S

	public Example getExample()
	{
		return example;
	}

	public String getText()
	{
		return text;
	}

	public Func getF()
	{
		return f;
	}

	@RequiresIdFrom(type = Rel.class)
	@Override
	public Integer getIntId()
	{
		return COLLECTOR.get(this);
	}

	// O R D E R

	@Override
	public int compareTo(@NotNull final Rel that)
	{
		return COMPARATOR.compare(this, that);
	}

	@Override
	public String dataRow()
	{
		// (relid),rel,func,exampleid
		return String.format("'%s',%s,%s", //
				Utils.escape(text), //
				Utils.nullable(f, Func::getSqlId), //
				example.getSqlId() //
		);
	}

	@Override
	public String toString()
	{
		return String.format("rel %s[%s][%s]", text, example, f);
	}
}
