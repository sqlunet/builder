package org.sqlbuilder.pb.objects;

import java.util.Map;
import java.util.TreeMap;

public class LexItem implements Comparable<LexItem>
{
	protected static final Map<LexItem, Word> map = new TreeMap<>();

	protected final String word;

	public LexItem(final String lemma)
	{
		this.word = lemma;
	}

	public static LexItem make(final String lemma)
	{
		return new LexItem(lemma);
	}

	public String getWord()
	{
		return this.word;
	}

	public void put()
	{
		final boolean keyExisted = LexItem.map.containsKey(this);
		LexItem.map.put(this, Word.make(this.word));
		if (keyExisted)
		{
			throw new RuntimeException(toString());
		}
	}

	// O R D E R

	@Override
	public int compareTo(final LexItem p)
	{
		return this.word.compareTo(p.word);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("%s", this.word);
	}
}
