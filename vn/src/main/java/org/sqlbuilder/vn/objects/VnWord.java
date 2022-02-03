package org.sqlbuilder.vn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.SetCollector;
import org.sqlbuilder.common.Utils;

import java.util.*;

public class VnWord implements Insertable, Comparable<VnWord>
{
	public static final Comparator<VnWord> COMPARATOR = Comparator.comparing(VnWord::getWord);

	public static final SetCollector<VnWord> COLLECTOR = new SetCollector<>(COMPARATOR);

	public final String word;

	// C O N S T R U C T

	public static VnWord make(final String word)
	{
		var w = new VnWord(word);
		COLLECTOR.add(w);
		return w;
	}

	private VnWord(final String word)
	{
		this.word = word;
	}

	// A C C E S S

	public String getWord()
	{
		return word;
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
		VnWord vnWord = (VnWord) that;
		return word.equals(vnWord.word);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(word);
	}

	// O R D E R

	@Override
	public String toString()
	{
		return word;
	}

	// O R D E R

	@Override
	public int compareTo(final VnWord that)
	{
		return COMPARATOR.compare(this, that);
	}

	@Override
	public String dataRow()
	{
		// this.id
		// wordid (or null)
		// word
		return String.format("%s,'%s'", "NULL", Utils.escape(word));
	}
}
