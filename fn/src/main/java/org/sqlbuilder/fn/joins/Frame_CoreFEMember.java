package org.sqlbuilder.fn.joins;


import org.sqlbuilder.fn.objects.FE;
import org.sqlbuilder.fn.objects.Frame;

/*
frames_corefes.table=fnframes_corefes
frames_corefes.create=CREATE TABLE IF NOT EXISTS %Fn_frames_corefes.table% ( frameid INTEGER NOT NULL,feid INTEGER NOT NULL,PRIMARY KEY (frameid,feid) );
frames_corefes.fk1=ALTER TABLE %Fn_frames_corefes.table% ADD CONSTRAINT fk_%Fn_frames_corefes.table%_frameid FOREIGN KEY (frameid) REFERENCES %Fn_frames.table% (frameid);
frames_corefes.fk2=ALTER TABLE %Fn_frames_corefes.table% ADD CONSTRAINT fk_%Fn_frames_corefes.table%_feid FOREIGN KEY (feid) REFERENCES %Fn_fes.table% (feid);
frames_corefes.no-fk1=ALTER TABLE %Fn_frames_corefes.table% DROP CONSTRAINT fk_%Fn_frames_corefes.table%_feid CASCADE;
frames_corefes.no-fk2=ALTER TABLE %Fn_frames_corefes.table% DROP CONSTRAINT fk_%Fn_frames_corefes.table%_frameid CASCADE;
frames_corefes.insert=INSERT INTO %Fn_frames_corefes.table% (frameid,feid) VALUES(?,?);
 */

public class Frame_CoreFEMember extends Pair<Frame, FE>
{
	public Frame_CoreFEMember(final Frame frameid, final FE feid)
	{
		super(frameid, feid);
	}

	@Override
	public String toString()
	{
		return String.format("[FR-coreFE frameid=%s feid=%s]", this.first, this.second);
	}
}
