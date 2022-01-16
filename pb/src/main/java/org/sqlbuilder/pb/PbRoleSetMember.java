package org.sqlbuilder.pb;

import org.sqlbuilder.common.Insertable;

import java.io.Serializable;

public class PbRoleSetMember implements Insertable<PbRoleSetMember>, Serializable
{
	final PbRoleSet roleSet;

	final PbWord pbWord;

	public PbRoleSetMember(final PbRoleSet roleSet, final PbWord pbWord)
	{
		this.roleSet = roleSet;
		this.pbWord = pbWord;
	}

	@Override
	public String dataRow()
	{
		// Long(1, this.roleSetId);
		// Long(2, this.pbWordId);
		return null;
	}
}
