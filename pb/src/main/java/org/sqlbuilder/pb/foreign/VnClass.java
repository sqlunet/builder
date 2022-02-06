package org.sqlbuilder.pb.foreign;

import java.util.Comparator;
import java.util.Objects;

public class VnClass implements Comparable<VnClass>
{
	static public final Comparator<VnClass> COMPARATOR = Comparator.comparing(VnClass::getClassId);

	private final String head;

	private final String className;

	// C O N S T R U C T O R

	public static VnClass make(final String head, final String className)
	{
		return new VnClass(head, className);
	}

	private VnClass(final String head, final String className)
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
		VnClass vnClass = (VnClass) o;
		return Objects.equals(head, vnClass.head) && className.equals(vnClass.className);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(head, className);
	}

	// O R D E R I N G

	@Override
	public int compareTo(final VnClass that)
	{
		return COMPARATOR.compare(this, that);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("<%s>", this.className);
	}
}
