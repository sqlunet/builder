package org.sqlbuilder.vn;

import org.sqlbuilder.common.Insertable;

public class VnRoleMapping implements Insertable<VnRoleMapping>
{
	private final VnRole role;

	private final VnClass clazz;

	// C O N S T R U C T

	public VnRoleMapping(final VnRole role, final VnClass clazz)
	{
		this.role = role;
		this.clazz = clazz;
	}

	// A C C E S S

	@Override
	public String toString()
	{
		return "classid=" + this.clazz + " roleid=" + this.role;
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
//		Long(1, VnRoleMapping.allocate());
//		Long(2, this.roleId);
//		Long(3, this.classId);
		return null;
	}
}
