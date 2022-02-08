package org.sqlbuilder.pb.objects;

import org.sqlbuilder.common.*;

import java.util.Comparator;
import java.util.Objects;

public class PbWord implements HasId, Insertable, Comparable<PbWord>
{
	public static final Comparator<PbWord> COMPARATOR = Comparator.comparing(PbWord::getWord);

	public static final SetCollector<PbWord> COLLECTOR = new SetCollector<>(COMPARATOR);

	public final String word;

	// C O N S T R U C T O R

	public static PbWord make(final String word)
	{
		var w = new PbWord(word);
		COLLECTOR.add(w);
		return w;
	}

	private PbWord(final String word)
	{
		this.word = word;
	}

	// A C C E S S

	public String getWord()
	{
		return word;
	}

	@RequiresIdFrom(type = PbWord.class)
	@Override
	public Integer getIntId()
	{
		return COLLECTOR.get(this);
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
		PbWord that = (PbWord) o;
		return word.equals(that.word);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(word);
	}

	// O R D E R

	@Override
	public int compareTo(final PbWord that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T

	@RequiresIdFrom(type = PbWord.class)
	@Override
	public String dataRow()
	{
		return String.format("%s,'%s'", //
				"NULL", //
				Utils.escape(word));
	}
}
