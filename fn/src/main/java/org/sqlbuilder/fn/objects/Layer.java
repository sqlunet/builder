package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.LayerType;

public class Layer implements Insertable<Layer>
{
	public static final Set<Layer> SET = new HashSet<>();

	public final LayerType layer;

	public final long annosetid;

	public Layer(final LayerType layer, final long annosetid)
	{
		this.layer = layer;
		this.annosetid = annosetid;
	}

	@Override
	public String dataRow()
	{
		return String.format("NULL,'%s',%d,%d", //
				//layer.getId(), //
				layer.getName(), //
				layer.getRank(), //
				annosetid);
	}

	@Override
	public String toString()
	{
		return String.format("[LAY name=%s annosetid=%s]", this.layer.getName(), this.annosetid);
	}
}
