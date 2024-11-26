package org.sqlbuilder.fn.objects;

import org.sqlbuilder.annotations.RequiresIdFrom;
import org.sqlbuilder.common.*;

import java.util.Comparator;

import edu.berkeley.icsi.framenet.GovernorType;

public class Governor implements HasId, Insertable
{
	public static final Comparator<Governor> COMPARATOR = Comparator.comparing(Governor::getWord).thenComparing(Governor::getType);

	public static final SetCollector2<Governor> COLLECTOR = new SetCollector2<>(COMPARATOR);

	private final String type;

	private final Word word;

	public static Governor make(final GovernorType governor)
	{
		var g = new Governor(governor);
		COLLECTOR.add(g);
		return g;
	}

	private Governor(final GovernorType governor)
	{
		this.type = governor.getType();
		this.word = Word.make(governor.getLemma());
	}

	// A C C E S S

	public String getType()
	{
		return type;
	}

	public String getWord()
	{
		return word.getWord();
	}

	@RequiresIdFrom(type = Governor.class)
	@Override
	public Integer getIntId()
	{
		return COLLECTOR.apply(this);
	}

	// I N S E R T

	@RequiresIdFrom(type = Word.class)
	@Override
	public String dataRow()
	{
		// governorid,governortype,fnwordid
		return String.format("'%s',%s", //
				Utils.escape(type), //
				word.getSqlId());
	}

	@Override
	public String comment()
	{
		return String.format("word=%s", word.getWord());
	}

	@Override
	public String toString()
	{
		return String.format("[GOV type=%s word=%s]", type, word);
	}
}