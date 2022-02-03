package org.sqlbuilder.vn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.common.SetCollector;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Comparator;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;

public class VnRole implements Insertable, Comparable<VnRole>
{
	public static final Comparator<VnRole> COMPARATOR = Comparator.comparing(VnRole::getRoleType).thenComparing(VnRole::getRestrs, Comparator.nullsFirst(Comparator.naturalOrder()));
	public static final SetCollector<VnRole> COLLECTOR = new SetCollector<>(COMPARATOR);

	private final VnRoleType roleType;

	private final VnRestrs restrs;

	// C O N S T R U C T

	public static VnRole make(final String type, final String restrsXML) throws ParserConfigurationException, SAXException, IOException
	{
		final VnRoleType roleType = VnRoleType.make(type);
		final VnRestrs restrs = restrsXML == null || restrsXML.isEmpty() || restrsXML.equals("<SELRESTRS/>") ? null : VnRestrs.make(restrsXML, false);
		var r = new VnRole(roleType, restrs);
		COLLECTOR.add(r);
		return r;
	}

	private VnRole(final VnRoleType roleType, final VnRestrs restrs)
	{
		this.roleType = roleType;
		this.restrs = restrs;
	}

	// A C C E S S

	public VnRoleType getRoleType()
	{
		return this.roleType;
	}

	public VnRestrs getRestrs()
	{
		return this.restrs;
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
		VnRole that = (VnRole) o;
		return roleType.equals(that.roleType) && Objects.equals(restrs, that.restrs);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(roleType, restrs);
	}


	// O R D E R I N G

	@Override
	public int compareTo(final VnRole that)
	{
		return COMPARATOR.compare(this, that);
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(this.roleType.getType());
		if (this.restrs != null)
		{
			sb.append(' ');
			sb.append(this.restrs);
		}
		return sb.toString();
	}

	// I N S E R T

	@RequiresIdFrom(type = VnRoleType.class)
	@RequiresIdFrom(type = VnRestrs.class)
	@Override
	public String dataRow()
	{
		// id
		// roleType.id
		// restrs.id (or null)
		return String.format("%d,%s", VnRoleType.COLLECTOR.get(roleType), restrs == null ? "NULL" : VnRestrs.COLLECTOR.get(restrs));
	}
}
