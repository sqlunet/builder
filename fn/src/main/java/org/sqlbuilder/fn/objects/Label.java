package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.LabelType;

/*
labels.table=fnlabels
labels.create=CREATE TABLE IF NOT EXISTS %Fn_labels.table% ( labelid INTEGER NOT NULL,layerid INTEGER,labeltypeid INTEGER DEFAULT NULL,labelitypeid INTEGER DEFAULT NULL,feid INTEGER DEFAULT NULL,start INTEGER DEFAULT NULL,end INTEGER DEFAULT NULL,fgcolor VARCHAR(6),bgcolor VARCHAR(6),cby VARCHAR(27),PRIMARY KEY (labelid) );
labels.altcreate1=ALTER TABLE %Fn_labels.table% ADD COLUMN labeltype VARCHAR(32) AFTER labeltypeid;
labels.fk1=ALTER TABLE %Fn_labels.table% ADD CONSTRAINT fk_%Fn_labels.table%_layerid FOREIGN KEY (layerid) REFERENCES %Fn_layers.table% (layerid);
labels.fk2=ALTER TABLE %Fn_labels.table% ADD CONSTRAINT fk_%Fn_labels.table%_feid FOREIGN KEY (feid) REFERENCES %Fn_fes.table% (feid);
labels.fk3=ALTER TABLE %Fn_labels.table% ADD CONSTRAINT fk_%Fn_labels.table%_labeltypeid FOREIGN KEY (labeltypeid) REFERENCES %Fn_labeltypes.table% (labeltypeid);
labels.fk4=ALTER TABLE %Fn_labels.table% ADD CONSTRAINT fk_%Fn_labels.table%_labelitypeid FOREIGN KEY (labelitypeid) REFERENCES %Fn_labelitypes.table% (labelitypeid);
labels.no-fk1=ALTER TABLE %Fn_labels.table% DROP CONSTRAINT fk_%Fn_labels.table%_layerid CASCADE;
labels.no-fk2=ALTER TABLE %Fn_labels.table% DROP CONSTRAINT fk_%Fn_labels.table%_feid CASCADE;
labels.no-fk3=ALTER TABLE %Fn_labels.table% DROP CONSTRAINT fk_%Fn_labels.table%_labeltypeid CASCADE;
labels.no-fk4=ALTER TABLE %Fn_labels.table% DROP CONSTRAINT fk_%Fn_labels.table%_labelitypeid CASCADE;
labels.insert=INSERT INTO %Fn_labels.table% (labelid,layerid,labeltype,labelitypeid,feid,start,end,fgcolor,bgcolor,cby) VALUES(?,?,?,?,?,?,?,?,?,?);
 */
public class Label implements Insertable<Label>
{
	public static final Set<Label> SET = new HashSet<>();

	private final String name;

	private final String itype;

	private final int feid;

	private final int start;

	private final int end;

	public final Layer layer;

	public static Label make(final LabelType label, final Layer layer)
	{
		var l = new Label(label, layer);
		SET.add(l);
		return l;
	}

	private Label(final LabelType label, final Layer layer)
	{
		this.name = label.getName();
		this.itype = label.getItype() == null ? null : label.getItype().toString();
		this.feid = label.getFeID();
		this.start = label.getStart();
		this.end = label.getEnd();
		this.layer = layer;
	}

	public static final Comparator<Label> COMPARATOR = Comparator.comparing(l -> l.name);

	@Override
	public String dataRow()
	{
		// (labelid),labeltype,labelitypeid,feid,start,end,layerid,fgcolor,bgcolor,cby
		return String.format("'%s',%s,%s,%s,%s,%s",
				name, //
				Utils.nullableEscapedString(itype), //
				Utils.zeroableInt(feid), //
				Utils.zeroableInt(start), //
				Utils.zeroableInt(end), //
				Layer.COLLECTOR.get(layer));
		// String(8, this.label.getBgColor());
		// String(9, this.label.getFgColor());
		// String(10, this.label.getCBy());
	}

	@Override
	public String toString()
	{
		return String.format("[LAB label=%s layer=%s]", this.name, this.layer);
	}
}
