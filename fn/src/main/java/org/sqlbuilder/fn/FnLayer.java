package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.LayerType;

public class FnLayer implements Insertable<FnLayer>
{
	public static final Set<FnLayer> SET = new HashSet<>();

	public final LayerType layer;

	public final FnAnnotationSet annosetid;

	public FnLayer(final FnAnnotationSet annoset, final LayerType layer)
	{
		this.annosetid = annoset;
		this.layer = layer;
	}

	@Override
	public String dataRow()
	{
		// Long(1, getId());
		// Long(2, this.annosetid);
		// String(3, this.layer.getName());
		// Int(4, this.layer.getRank());
		return null;
	}

	@Override
	public String toString()
	{
		return String.format("[LAY name=%s annoset=%s]", this.layer.getName(), this.annosetid);
	}
}
