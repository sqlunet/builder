package org.sqlbuilder.fn.joins;


import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

/*
frames_corefes.table=fnframes_corefes
frames_corefes.create=CREATE TABLE IF NOT EXISTS %Fn_frames_corefes.table% ( frameid INTEGER NOT NULL,feid INTEGER NOT NULL,PRIMARY KEY (frameid,feid) );
frames_corefes.fk1=ALTER TABLE %Fn_frames_corefes.table% ADD CONSTRAINT fk_%Fn_frames_corefes.table%_frameid FOREIGN KEY (frameid) REFERENCES %Fn_frames.table% (frameid);
frames_corefes.fk2=ALTER TABLE %Fn_frames_corefes.table% ADD CONSTRAINT fk_%Fn_frames_corefes.table%_feid FOREIGN KEY (feid) REFERENCES %Fn_fes.table% (feid);
frames_corefes.no-fk1=ALTER TABLE %Fn_frames_corefes.table% DROP CONSTRAINT fk_%Fn_frames_corefes.table%_feid CASCADE;
frames_corefes.no-fk2=ALTER TABLE %Fn_frames_corefes.table% DROP CONSTRAINT fk_%Fn_frames_corefes.table%_frameid CASCADE;
frames_corefes.insert=INSERT INTO %Fn_frames_corefes.table% (frameid,feid) VALUES(?,?);
 */

// TODO remove
public class Frame_CoreFEMember extends Pair<Integer, Integer> implements Insertable<Frame_CoreFEMember>
{
	public static final Set<Frame_CoreFEMember> SET = new HashSet<>();

	// C O N S T R U C T O R

	public static Frame_CoreFEMember make(final int frameid, final int feid)
	{
		var fe = new Frame_CoreFEMember(frameid, feid);
		SET.add(fe);
		return fe;
	}

	private Frame_CoreFEMember(final int frameid, final int feid)
	{
		super(frameid, feid);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("%d,%d", first, second);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[FR-coreFE frameid=%s feid=%s]", this.first, this.second);
	}
}
