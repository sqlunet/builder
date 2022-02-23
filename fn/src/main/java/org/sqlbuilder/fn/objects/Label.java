package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.fn.types.LabelType;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Label implements Insertable
{
	public static final Set<Label> SET = new HashSet<>();

	private final String name;

	private final Integer itypeid;

	private final int feid;

	private final int start;

	private final int end;

	public final Layer layer;

	@SuppressWarnings("UnusedReturnValue")
	public static Label make(final edu.berkeley.icsi.framenet.LabelType label, final Layer layer)
	{
		var l = new Label(label, layer);
		LabelType.add(l.name);
		SET.add(l);
		return l;
	}

	private Label(final edu.berkeley.icsi.framenet.LabelType label, final Layer layer)
	{
		this.name = label.getName();
		this.itypeid = label.getItype() == null ? null : label.getItype().intValue();
		this.feid = label.getFeID();
		this.start = label.getStart();
		this.end = label.getEnd();
		this.layer = layer;
	}

	public static final Comparator<Label> COMPARATOR = Comparator.comparing(l -> l.name);

	@RequiresIdFrom(type = Layer.class)
	@RequiresIdFrom(type = LabelType.class)
	@Override
	public String dataRow()
	{
		// (labelid),labeltype,labelitypeid,feid,start,end,layerid,fgcolor,bgcolor,cby
		return String.format("%d,%s,%s,%s,%s,%s", //
				LabelType.getIntId(name), //
				Utils.nullableInt(itypeid), //
				Utils.zeroableInt(feid), //
				Utils.zeroableInt(start), //
				Utils.zeroableInt(end), //
				layer.getSqlId());
	}

	@Override
	public String comment()
	{
		return String.format("type=%s", name);
	}

	@Override
	public String toString()
	{
		return String.format("[LAB label=%s layer=%s]", this.name, this.layer);
	}
}
