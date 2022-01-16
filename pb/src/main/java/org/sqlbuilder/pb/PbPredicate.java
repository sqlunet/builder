package org.sqlbuilder.pb;

public class PbPredicate extends PbLexItem
{
	private final String head;

	public PbPredicate(final String head, final String lemma)
	{
		super(lemma);
		this.head = head;
	}

	public static PbPredicate make(final String head, final String lemmaAttribute)
	{
		return new PbPredicate(head, lemmaAttribute);
	}

	public String getHead()
	{
		return this.head;
	}
}
