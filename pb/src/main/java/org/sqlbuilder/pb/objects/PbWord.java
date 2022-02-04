package org.sqlbuilder.pb.objects;

import org.sqlbuilder.common.*;

import java.util.Comparator;

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
		return String.format("%d,%s,'%s'", //
				COLLECTOR.get(this), //
				"NULL", //
				Utils.escape(word));
	}
}
