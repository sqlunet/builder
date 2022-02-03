package org.sqlbuilder.vn;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

public class VnClassDataMapping implements Insertable
{
	protected static final Set<VnClassDataMapping> SET = new HashSet<>();

	final VnClassData classData;

	public VnClassDataMapping(final VnClassData classData)
	{
		this.classData = classData;
	}

	@Override
	public String toString()
	{
		return String.format("classdata=[%s]", this.classData);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		//		final long classId = this.classData.vnclass.getId();
		//
		//		// map (word,synset)->(role,rolesetid)
		//		for (final VnRole role : this.classData.getRoles())
		//		{
		//			final VnRoleMapping roleMap = new VnRoleMapping(role.getId(), classId);
		//			roleMap.insert(connection);
		//		}
		//
		//		// map (word,synset)->(frameid,classid)
		//		for (final VnFrame frame : this.classData.getFrames())
		//		{
		//			final VnFrameMapping frameMap = new VnFrameMapping(frame.getId(), classId);
		//			frameMap.insert(connection);
		//		}
		return null;
	}
}
