package org.sqlbuilder.pb.foreign;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.pb.objects.Role;

import java.util.List;
import java.util.Map;

public class PbRole_VnRole implements Insertable
{
	public static Map<Role, List<PbVnRoleMapping>> map;

	public static Map<Role, PbVnRoleMapping> semlinkMap;

	public final RoleIds pbRoleIds;

	public final RoleIds vnRoleIds;

	public PbRole_VnRole(final RoleIds pbRoleIds, final RoleIds vnRoleIds)
	{
		this.pbRoleIds = pbRoleIds;
		this.vnRoleIds = vnRoleIds;
	}

	@Override
	public String dataRow()
	{
		// rolesetid,roleid,vnclassid,vnroleid
		//		Long(1, this.pbRoleIds.setId);
		//		Long(2, this.pbRoleIds.id);
		//		Long(3, this.vnRoleIds.setId);
		//		Long(4, this.vnRoleIds.id);
		return null;
	}

	@Override
	public String toString()
	{
		return String.format("%s-%s > %s-%s", this.pbRoleIds.setId, this.pbRoleIds.id, this.vnRoleIds.setId, this.vnRoleIds.id);
	}
}
