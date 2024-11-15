package org.sqlbuilder.pb.objects;

public class Predicate extends LexItem
{
	private final String head;

	public static Predicate make(final String head, final String lemma)
	{
		//assert head.equals(lemma) : head + "!=" + lemma;
		return new Predicate(head, lemma);
	}

	private Predicate(final String head, final String lemma)
	{
		super(lemma);
		this.head = head;
	}

	public String getHead()
	{
		return this.head;
	}
}
