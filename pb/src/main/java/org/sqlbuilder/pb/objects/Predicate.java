package org.sqlbuilder.pb.objects;

public class Predicate extends LexItem
{
	private final String head;

	public Predicate(final String head, final String lemma)
	{
		super(lemma);
		this.head = head;
	}

	public static Predicate make(final String head, final String lemmaAttribute)
	{
		return new Predicate(head, lemmaAttribute);
	}

	public String getHead()
	{
		return this.head;
	}
}
