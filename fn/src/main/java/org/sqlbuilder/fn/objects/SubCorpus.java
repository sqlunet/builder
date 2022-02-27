package org.sqlbuilder.fn.objects;

import org.sqlbuilder.annotations.RequiresIdFrom;
import org.sqlbuilder.common.*;

import java.util.Comparator;

import edu.berkeley.icsi.framenet.SubCorpusType;

public class SubCorpus implements HasId, Insertable
{
	public static final Comparator<SubCorpus> COMPARATOR = Comparator.comparing(SubCorpus::getName).thenComparing(SubCorpus::getLuid);

	public static final SetCollector<SubCorpus> COLLECTOR = new SetCollector<>(COMPARATOR);

	private final String name;

	private final int luid;

	public static SubCorpus make(final SubCorpusType subcorpus, final int luid)
	{
		var c = new SubCorpus(subcorpus.getName(), luid);
		COLLECTOR.add(c);
		return c;
	}

	private SubCorpus(final String name, final int luid)
	{
		this.name = name;
		this.luid = luid;
	}

	// I D

	public String getName()
	{
		return name;
	}

	public int getLuid()
	{
		return luid;
	}

	@RequiresIdFrom(type = SubCorpus.class)
	@Override
	public Integer getIntId()
	{
		return COLLECTOR.get(this);
	}

	// I N S E R T

	@RequiresIdFrom(type = SubCorpus.class)
	@Override
	public String dataRow()
	{
		return String.format("'%s',%d", //
				Utils.escape(name), //
				luid);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[SUBCORPUS name=%s]", name);
	}
}
