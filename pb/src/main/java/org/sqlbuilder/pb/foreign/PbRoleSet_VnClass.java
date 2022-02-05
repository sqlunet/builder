package org.sqlbuilder.pb.foreign;

import java.util.Collection;
import java.util.Set;

public class PbRoleSet_VnClass implements Comparable<PbRoleSet_VnClass>
{
	private final String head;

	private final String className;

	// C O N S T R U C T O R

	public static PbRoleSet_VnClass make(final String head, final String className)
	{
		return new PbRoleSet_VnClass(head, className);
	}

	private PbRoleSet_VnClass(final String head, final String className)
	{
		this.head = head;
		this.className = className;
	}

	// A C C E S S

	public String getHead()
	{
		return this.head;
	}

	public String getClassName()
	{
		return this.className;
	}

	public String getClassId()
	{
		return String.format("%s-%s", this.head == null ? "%" : this.head, this.className);
	}

	// O R D E R I N G

	@Override
	public int compareTo(final PbRoleSet_VnClass vnc)
	{
		return this.className.compareTo(vnc.className);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("<%s>", this.className);
	}

	public static String toString(final Collection<PbRoleSet_VnClass> vnClasses)
	{
		final StringBuilder sb = new StringBuilder();
		sb.append('{');
		sb.append(' ');
		for (final PbRoleSet_VnClass vnClass : vnClasses)
		{
			sb.append(vnClass);
			sb.append(' ');
		}
		sb.append('}');
		return sb.toString();
	}

	public static String toString(final Set<String> words)
	{
		final StringBuilder sb = new StringBuilder();
		sb.append('{');
		sb.append(' ');
		for (final String word : words)
		{
			sb.append(word);
			sb.append(' ');
		}
		sb.append('}');
		return sb.toString();
	}
}
