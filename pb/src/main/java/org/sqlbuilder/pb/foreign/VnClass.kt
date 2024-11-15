package org.sqlbuilder.pb.foreign;

import org.sqlbuilder.common.NotNull;

import java.util.Comparator;
import java.util.Objects;

public class VnClass implements Comparable<VnClass>
{
	static public final Comparator<VnClass> COMPARATOR = Comparator.comparing(VnClass::getClassName);

	private final String head;

	private final String classTag;

	// C O N S T R U C T O R

	public static VnClass make(final String head, final String classTag)
	{
		return new VnClass(head, classTag);
	}

	private VnClass(final String head, final String className)
	{
		this.head = head;
		this.classTag = className;
	}

	// A C C E S S

	public String getHead()
	{
		return this.head;
	}

	public String getClassTag()
	{
		return this.classTag;
	}

	public String getClassName()
	{
		return String.format("%s-%s", head == null ? "%" : head, classTag);
	}

	// I D E N T I T Y

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		VnClass that = (VnClass) o;
		return Objects.equals(head, that.head) && classTag.equals(that.classTag);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(head, classTag);
	}

	// O R D E R I N G

	@Override
	public int compareTo(@NotNull final VnClass that)
	{
		return COMPARATOR.compare(this, that);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("<%s>", classTag);
	}
}
