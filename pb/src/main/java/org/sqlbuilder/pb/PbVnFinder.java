package org.sqlbuilder.pb;

import org.sqlbuilder.common.NotFoundException;

import java.util.Collection;
import java.util.Set;

public class PbVnFinder
{
	public static PbVnClass findVnClass(final String className) throws NotFoundException
	{
		throw new NotFoundException(className);
	}

	public static Collection<PbVnClass> findVnClasses(final String className) throws NotFoundException
	{
		throw new NotFoundException(className);
	}

	public static RoleIds findVnRole(final PbVnRole vnRole) throws NotFoundException
	{
		throw new NotFoundException(vnRole.toString());
	}

	public static Collection<PbVnClass> findVnClassesForLemma(final String head) throws NotFoundException
	{
		throw new NotFoundException(head);
	}

	public static Set<String> findLemmasForClass(final String classId) throws NotFoundException
	{
		throw new NotFoundException(classId);
	}
}
