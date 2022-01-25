package org.sqlbuilder.fn.joins;

import java.util.HashSet;
import java.util.Set;

/*
frames_semtypes.table=fnframes_semtypes
frames_semtypes.create=CREATE TABLE IF NOT EXISTS %Fn_frames_semtypes.table% ( frameid INTEGER NOT NULL,semtypeid INTEGER NOT NULL,PRIMARY KEY (frameid,semtypeid) );
frames_semtypes.fk1=ALTER TABLE %Fn_frames_semtypes.table% ADD CONSTRAINT fk_%Fn_frames_semtypes.table%_frameid FOREIGN KEY (frameid) REFERENCES %Fn_frames.table% (frameid);
frames_semtypes.fk2=ALTER TABLE %Fn_frames_semtypes.table% ADD CONSTRAINT fk_%Fn_frames_semtypes.table%_semtypeid FOREIGN KEY (semtypeid) REFERENCES %Fn_semtypes.table% (semtypeid);
frames_semtypes.no-fk1=ALTER TABLE %Fn_frames_semtypes.table% DROP CONSTRAINT fk_%Fn_frames_semtypes.table%_frameid CASCADE;
frames_semtypes.no-fk2=ALTER TABLE %Fn_frames_semtypes.table% DROP CONSTRAINT fk_%Fn_frames_semtypes.table%_semtypeid CASCADE;
frames_semtypes.insert=INSERT INTO %Fn_frames_semtypes.table% (frameid,semtypeid) VALUES(?,?);
 */
public class Frame_SemType extends Pair<Integer, Integer>
{
	public static final Set<Frame_SemType> SET = new HashSet<>();

	public Frame_SemType(final int frameid, final int semtypeid)
	{
		super(frameid, semtypeid);
		SET.add(this);
	}

	@Override
	public String toString()
	{
		return String.format("[FR-SEM frameid=%s semtypeid=%s]", this.first, this.second);
	}
}
