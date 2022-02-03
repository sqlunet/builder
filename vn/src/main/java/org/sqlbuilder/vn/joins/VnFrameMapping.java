package org.sqlbuilder.vn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.vn.objects.VnClass;
import org.sqlbuilder.vn.objects.VnFrame;

public class VnFrameMapping implements Insertable
{
	private final VnFrame frame;

	private final VnClass clazz;

	// C O N S T R U C T

	public VnFrameMapping(final VnFrame frame, final VnClass clazz)
	{
		this.frame = frame;
		this.clazz = clazz;
	}

	// I N S E R T

	@RequiresIdFrom(type = VnFrame.class)
	@Override
	public String dataRow()
	{
		//		Long(1, VnFrameMapping.allocate());
		//		Long(2, this.classId);
		//		Long(3, this.frameId);
		return null;
	}
}
