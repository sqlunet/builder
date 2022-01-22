package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
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
