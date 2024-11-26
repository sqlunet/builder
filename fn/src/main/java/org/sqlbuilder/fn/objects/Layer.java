package org.sqlbuilder.fn.objects;

import org.sqlbuilder.annotations.RequiresIdFrom;
import org.sqlbuilder.common.HasId;
import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.SetCollector2;
import org.sqlbuilder.fn.types.LayerType;

import java.util.Comparator;

public class Layer implements HasId, Insertable
{
	public static final Comparator<Layer> COMPARATOR = Comparator.comparing(Layer::getName).thenComparing(Layer::getAnnosetid);

	public static final SetCollector2<Layer> COLLECTOR = new SetCollector2<>(COMPARATOR);

	private final String name;

	private final int rank;

	public final long annosetid;

	public static Layer make(final edu.berkeley.icsi.framenet.LayerType layer, final int annosetid)
	{
		var l = new Layer(layer, annosetid);
		LayerType.add(l.name);
		COLLECTOR.add(l);
		return l;
	}

	private Layer(final edu.berkeley.icsi.framenet.LayerType layer, final int annosetid)
	{
		this.name = layer.getName();
		this.rank = layer.getRank();
		this.annosetid = annosetid;
	}

	public String getName()
	{
		return name;
	}

	public long getAnnosetid()
	{
		return annosetid;
	}

	@RequiresIdFrom(type = Layer.class)
	@Override
	public Integer getIntId()
	{
		return COLLECTOR.apply(this);
	}

	@RequiresIdFrom(type = Layer.class)
	@Override
	public String dataRow()
	{
		return String.format("%d,%s,%d,%d", //
				getIntId(), //
				LayerType.getIntId(name), //
				rank, //
				annosetid);
	}

	@Override
	public String comment()
	{
		return String.format("type=%s", name);
	}

	@Override
	public String toString()
	{
		return String.format("[LAY name=%s annosetid=%s]", this.name, this.annosetid);
	}
}
