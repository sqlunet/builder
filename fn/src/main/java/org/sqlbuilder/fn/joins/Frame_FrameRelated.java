package org.sqlbuilder.fn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.types.FrameRelation;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.FrameIDNameType;

/*
frames_related.table=fnframes_related
frames_related.create=CREATE TABLE IF NOT EXISTS %Fn_frames_related.table% ( frameid INTEGER NOT NULL,frame2id INTEGER NOT NULL,relationid INTEGER NOT NULL );
frames_related.altcreate1=ALTER TABLE %Fn_frames_related.table% ADD COLUMN frame2 VARCHAR(40) AFTER frame2id;
frames_related.altcreate2=ALTER TABLE %Fn_frames_related.table% ADD COLUMN relation VARCHAR(40) AFTER relationid;
frames_related.pk=ALTER TABLE %Fn_frames_related.table% ADD CONSTRAINT pk_%Fn_frames_related.table% PRIMARY KEY (frameid,frame2id,relationid);
frames_related.no-pk=ALTER TABLE %Fn_frames_related.table% DROP PRIMARY KEY;
frames_related.fk1=ALTER TABLE %Fn_frames_related.table% ADD CONSTRAINT fk_%Fn_frames_related.table%_frameid FOREIGN KEY (frameid) REFERENCES %Fn_frames.table% (frameid);
frames_related.fk2=ALTER TABLE %Fn_frames_related.table% ADD CONSTRAINT fk_%Fn_frames_related.table%_frame2id FOREIGN KEY (frame2id) REFERENCES %Fn_frames.table% (frameid);
frames_related.fk3=ALTER TABLE %Fn_frames_related.table% ADD CONSTRAINT fk_%Fn_frames_related.table%_relationid FOREIGN KEY (relationid) REFERENCES %Fn_framerelations.table% (relationid);
frames_related.no-fk1=ALTER TABLE %Fn_frames_related.table% DROP CONSTRAINT fk_%Fn_frames_related.table%_frame2id CASCADE;
frames_related.no-fk2=ALTER TABLE %Fn_frames_related.table% DROP CONSTRAINT fk_%Fn_frames_related.table%_frameid CASCADE;
frames_related.no-fk3=ALTER TABLE %Fn_frames_related.table% DROP CONSTRAINT fk_%Fn_frames_related.table%_relationid CASCADE;
frames_related.insert=INSERT INTO %Fn_frames_related.table% (frameid,frame2id,frame2,relationid,relation) VALUES(?,-1,?,-1,?);
 */
public class Frame_FrameRelated extends Pair<Integer, Integer> implements Insertable<Frame_FrameRelated>
{
	public static final Set<Frame_FrameRelated> SET = new HashSet<>();

	private final String relation;

	// C O N S T R U C T O R

	public static Frame_FrameRelated make(final int frameid, final FrameIDNameType frame2, final String relation)
	{
		var ff = new Frame_FrameRelated(frameid, frame2.getID(), relation);
		FrameRelation.add(relation);
		SET.add(ff);
		return ff;
	}

	private Frame_FrameRelated(final int frameid, final int frame2id, final String relation)
	{
		super(frameid, frame2id);
		this.relation = relation;
	}

	// A C C E S S

	public String getRelation()
	{
		return relation;
	}

	// O R D E R

	public static Comparator<Frame_FrameRelated> COMPARATOR = Comparator.comparing(Frame_FrameRelated::getRelation).thenComparing(Pair::getFirst).thenComparing(Pair::getSecond);

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("%d,%d,%s", //
				first, //
				second, //
				FrameRelation.getId(relation));
	}

	@Override
	public String comment()
	{
		return String.format("%s", relation);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[FRrel frameid=%s frame2id=%s type=%s]", this.first, this.second, this.relation);
	}
}
