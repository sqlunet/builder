package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.LabelType;

public class Label implements Insertable<Label>
{
	public static final Set<Label> SET = new HashSet<>();

	public final LabelType label;

	public final Layer layer;

	public Label(final LabelType label, final Layer layerid)
	{
		this.layer = layerid;
		this.label = label;
	}

	@Override
	public String dataRow()
	{
		// labelid,layerid,labeltype,labelitypeid,feid,start,end,fgcolor,bgcolor,cby
		final int feid = this.label.getFeID();
		final int from = this.label.getStart();
		final int to = this.label.getEnd();

		// Long(1, getId());
		// Long(2, this.layerid);
		// String(3, this.label.getName());
		if (this.label.getItype() != null)
		{
			//Int(4, this.label.getItype().intValue());
		}
		else
		{
			//Null(4, Types.INTEGER);
		}
		if (feid != 0)
		{
			//Int(5, feid);
		}
		else
		{
			// Null(5, Types.INTEGER);
		}
		if (from == 0 && to == 0)
		{
			// Null(6, from);
			// Null(7, to);
		}
		else
		{
			// Int(6, from);
			// Int(7, to);
		}
		// String(8, this.label.getBgColor());
		// String(9, this.label.getFgColor());
		// String(10, this.label.getCBy());
		return null;
	}

	@Override
	public String toString()
	{
		return String.format("[LAB label=%s layer=%s]", this.label.getName(), this.layer);
	}
}
