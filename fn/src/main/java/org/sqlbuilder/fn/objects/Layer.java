package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.berkeley.icsi.framenet.LayerType;

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
public class Layer implements Insertable<Layer>
{
	public static final Set<Layer> SET = new HashSet<>();

	public static Map<Layer, Integer> MAP;

	private final String name;

	private final int rank;

	public final long annosetid;

	public static Layer make(final LayerType layer, final long annosetid)
	{
		var l = new Layer(layer, annosetid);
		SET.add(l);
		return l;
	}

	private Layer(final LayerType layer, final long annosetid)
	{
		this.name = layer.getName();
		this.rank = layer.getRank();
		this.annosetid = annosetid;
	}

	public static final Comparator<Layer> COMPARATOR = Comparator.comparing(l -> l.name);

	@Override
	public String dataRow()
	{
		return String.format("NULL,'%s',%d,%d", //
				//layer.getId(), //
				name, //
				rank, //
				annosetid);
	}

	@Override
	public String toString()
	{
		return String.format("[LAY name=%s annosetid=%s]", this.name, this.annosetid);
	}
}
