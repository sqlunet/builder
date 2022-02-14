package org.sqlbuilder.vn.objects;

import org.sqlbuilder.common.*;

import java.util.*;

public class Word implements HasId, Insertable, Resolvable<String,Integer>, Comparable<Word>
{
	public static final Comparator<Word> COMPARATOR = Comparator.comparing(Word::getWord);

	public static final SetCollector<Word> COLLECTOR = new SetCollector<>(COMPARATOR);

	public final String word;

	// C O N S T R U C T O R

	public static Word make(final String word)
	{
		var w = new Word(word);
		COLLECTOR.add(w);
		return w;
	}

	private Word(final String word)
	{
		this.word = word;
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
		return COLLECTOR.get(this);
	}

	// I D E N T I T Y

	@Override
	public boolean equals(final Object that)
	{
		if (this == that)
		{
			return true;
		}
		if (that == null || getClass() != that.getClass())
		{
			return false;
		}
		Word vnWord = (Word) that;
		return word.equals(vnWord.word);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(word);
	}

	// O R D E R

	@Override
	public int compareTo(final Word that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("'%s'", Utils.escape(word));
	}

	// I N S E R T

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
