package org.sqlbuilder.vn;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class VnWord implements Insertable<VnWord>, Comparable<VnWord>
{
	protected static final Set<VnWord> SET = new HashSet<>();

	public static Map<VnWord, Integer> MAP;

	public final String word;

	public VnWord(final String word)
	{
		this.word = word;
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
		return this.word.compareTo(that.word);
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
