package org.sqlbuilder.pb;

import java.util.Map;
import java.util.TreeMap;

public class PbLexItem implements Comparable<PbLexItem>
{
	protected static final Map<PbLexItem, PbWord> map = new TreeMap<>();

	protected final String lemma;

	public PbLexItem(final String lemma)
	{
		this.lemma = lemma;
	}

	public static PbLexItem make(final String lemma)
	{
		return new PbLexItem(lemma);
	}

	public String getLemma()
	{
		return this.lemma;
	}

	public void put()
	{
		final boolean keyExisted = PbLexItem.map.containsKey(this);
		PbLexItem.map.put(this, null);
		if (keyExisted)
			throw new RuntimeException(toString());
	}

	public void put(final PbWord word)
	{
		PbLexItem.map.put(this, word);
	}

	// O R D E R

	@Override
	public int compareTo(final PbLexItem p)
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
