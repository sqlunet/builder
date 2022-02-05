package org.sqlbuilder.pb;

import org.sqlbuilder.common.NotFoundException;
import org.sqlbuilder.pb.foreign.PbRoleSet_VnClass;
import org.sqlbuilder.pb.foreign.PbVnRoleMapping;
import org.sqlbuilder.pb.foreign.RoleIds;

import java.util.Collection;
import java.util.Set;

public class PbVnFinder
{
	public static PbRoleSet_VnClass findVnClass(final String className) throws NotFoundException
	{
		throw new NotFoundException(className);
	}

	public static Collection<PbRoleSet_VnClass> findVnClasses(final String className) throws NotFoundException
	{
		throw new NotFoundException(className);
	}

	public static RoleIds findVnRole(final PbVnRoleMapping vnRole) throws NotFoundException
	{
		throw new NotFoundException(vnRole.toString());
	}

	public static Collection<PbRoleSet_VnClass> findVnClassesForLemma(final String head) throws NotFoundException
	{
		throw new NotFoundException(head);
	}

	public static Set<String> findLemmasForClass(final String classId) throws NotFoundException
	{
		throw new NotFoundException(classId);
	}
}
