package org.sqlbuilder.fn.objects;

import org.sqlbuilder.annotations.RequiresIdFrom;
import org.sqlbuilder.common.*;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

public class Word implements HasId, Insertable, Resolvable<String, Integer>, Comparable<Word>, Serializable
{
	public static final Comparator<Word> COMPARATOR = Comparator.comparing(Word::getWord);

	public static final SetCollector<Word> COLLECTOR = new SetCollector<>(COMPARATOR);

	private final String word;

	// C O N S T R U C T O R

	public static Word make(final String lemma)
	{
		var w = new Word(lemma);
		Word.COLLECTOR.add(w);
		return w;
	}

	private Word(final String lemma)
	{
		this.word = lemma.toLowerCase(Locale.ENGLISH);
	}

	// A C C E S S

	public String getWord()
	{
		return word;
	}

	@RequiresIdFrom(type = Word.class)
	@Override
	public Integer getIntId()
	{
		return COLLECTOR.apply(this);
	}

	// I D E N T I T Y

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		Word word1 = (Word) o;
		return word.equals(word1.word);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(word);
	}

	// O R D E R

	@Override
	public int compareTo(@NotNull final Word that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("'%s'", Utils.escape(word));
	}

	// R E S O L V E

	@Override
	public String resolving()
	{
		return word;
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return word;
	}
}
