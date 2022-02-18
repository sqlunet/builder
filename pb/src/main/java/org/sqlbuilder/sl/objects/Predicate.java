package org.sqlbuilder.sl.objects;

public class Predicate
{
	public final String lemma;

	public static Predicate make(final String lemma, final String other)
	{
		return new Predicate(lemma);
	}

	private Predicate(final String lemma)
	{
		this.lemma = lemma;
	}
}
