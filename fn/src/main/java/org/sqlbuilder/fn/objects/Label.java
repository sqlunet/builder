package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.LabelType;

public class Label implements Insertable<Label>
{
	public static final Set<Label> SET = new HashSet<>();

	public final LabelType label;

	public final Layer layer;

	public Label(final LabelType label, final Layer layer)
	{
		this.label = label;
		this.layer = layer;
	}

	@Override
	public String dataRow()
	{
		// labelid,labeltype,labelitypeid,feid,start,end,layerid,fgcolor,bgcolor,cby
		return String.format("NULL,'%s',%s,%s,%s,%s,NULL",
				// getId(), //
				label.getName(), //
				Utils.nullableString(label.getItype().toString()), //
				Utils.zeroableInt(label.getFeID()), //
				Utils.zeroableInt(label.getStart()), //
				Utils.zeroableInt(label.getEnd())
				// ,layer.getId()
		);
		// String(8, this.label.getBgColor());
		// String(9, this.label.getFgColor());
		// String(10, this.label.getCBy());
	}

	@Override
	public String toString()
	{
		return String.format("[LAB label=%s layer=%s]", this.label.getName(), this.layer);
	}
}
