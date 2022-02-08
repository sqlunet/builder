package org.sqlbuilder.pb.objects;

import java.util.Map;
import java.util.TreeMap;

public class LexItem implements Comparable<LexItem>
{
	protected static final Map<LexItem, PbWord> map = new TreeMap<>();

	protected final String lemma;

	public LexItem(final String lemma)
	{
		this.lemma = lemma;
	}

	public static LexItem make(final String lemma)
	{
		return new LexItem(lemma);
	}

	public String getLemma()
	{
		return this.lemma;
	}

	public void put()
	{
		final boolean keyExisted = LexItem.map.containsKey(this);
		LexItem.map.put(this, null);
		if (keyExisted)
			throw new RuntimeException(toString());
	}

	public void put(final PbWord word)
	{
		LexItem.map.put(this, word);
	}

	// O R D E R

	@Override
	public int compareTo(final LexItem p)
	{
		return this.lemma.toString().compareTo(p.lemma.toString());
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("%s", this.lemma);
	}
}