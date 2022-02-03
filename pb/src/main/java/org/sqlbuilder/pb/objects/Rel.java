package org.sqlbuilder.pb.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.pb.PbNormalizer;

import java.util.*;

public class Rel implements Insertable, Comparable<Rel>
{
	public static final Set<Rel> SET = new HashSet<>();

	public static Map<Rel, Integer> MAP;

	private final String text;

	private final String f;

	private final Example example;

	public Rel(final Example example, final String rel, final String f)
	{
		this.text = PbNormalizer.normalize(rel);
		this.f = f;
		this.example = example;
		SET.add(this);
	}

	public static Rel make(final Example example, final String rel, final String f)
	{
		return new Rel(example, rel, f);
	}

	public Example getExample()
	{
		return this.example;
	}

	public String getText()
	{
		return this.text;
	}

	public String getF()
	{
		return this.f;
	}

	// O R D E R

	private static final Comparator<Rel> COMPARATOR = Comparator //
			.comparing(Rel::getExample) //
			.thenComparing(Rel::getText) //
			.thenComparing(Rel::getF);

	@Override
	public int compareTo(final Rel that)
	{
		return COMPARATOR.compare(this, that);
	}

	@Override
	public String dataRow()
	{
			// relid,rel,func,exampleid
//			Long(1, this.id);
//			String(2, this.text);
//			Null(3, Types.INTEGER);
//			Long(3, fId);
//			Long(4, this.example.getId());
			return null;
	}

	@Override
	public String toString()
	{
		return String.format("rel %s[%s][%s]", this.text, this.example, this.f);
	}
}
