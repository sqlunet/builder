package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.SetCollector;
import org.sqlbuilder.fn.HasId;
import org.sqlbuilder.fn.RequiresIdFrom;
import org.sqlbuilder.fn.types.LayerType;

import java.util.Comparator;


/*
layers.table=fnlayers
layers.create=CREATE TABLE IF NOT EXISTS %Fn_layers.table% ( layerid INTEGER NOT NULL,annosetid INTEGER NOT NULL,layertypeid INTEGER,`rank` INTEGER,PRIMARY KEY (layerid) );
layers.altcreate1=ALTER TABLE %Fn_layers.table% ADD COLUMN layertype VARCHAR(6) AFTER layertypeid;
layers.fk1=ALTER TABLE %Fn_layers.table% ADD CONSTRAINT fk_%Fn_layers.table%_annosetid FOREIGN KEY (annosetid) REFERENCES %Fn_annosets.table% (annosetid);
layers.fk2=ALTER TABLE %Fn_layers.table% ADD CONSTRAINT fk_%Fn_layers.table%_layertypeid FOREIGN KEY (layertypeid) REFERENCES %Fn_layertypes.table% (layertypeid);
layers.no-fk1=ALTER TABLE %Fn_layers.table% DROP CONSTRAINT fk_%Fn_layers.table%_annosetid CASCADE;
layers.no-fk2=ALTER TABLE %Fn_layers.table% DROP CONSTRAINT fk_%Fn_layers.table%_layertypeid CASCADE;
layers.insert=INSERT INTO %Fn_layers.table% (layerid,annosetid,layertype,`rank`) VALUES(?,?,?,?);
 */
public class Layer implements HasId, Insertable<Layer>
{
	public static final Comparator<Layer> COMPARATOR = Comparator.comparing(Layer::getName).thenComparing(Layer::getAnnosetid);

	public static final SetCollector<Layer> COLLECTOR = new SetCollector<>(COMPARATOR);

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
		return COLLECTOR.get(this);
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
		return  String.format("type=%s", name);
	}

	@Override
	public String toString()
	{
		return String.format("[LAY name=%s annosetid=%s]", this.name, this.annosetid);
	}
}
