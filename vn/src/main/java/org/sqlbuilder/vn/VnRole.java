package org.sqlbuilder.vn;

import org.sqlbuilder.common.Insertable;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

public class VnRole implements Insertable, Comparable<VnRole>
{
	protected static final Set<VnRole> SET = new HashSet<>();

	public static Map<VnRole, Integer> MAP;

	private final VnRoleType roleType;

	private final VnRestrs restrs;

	// C O N S T R U C T

	private VnRole(final VnRoleType roleType, final VnRestrs restrs)
	{
		this.roleType = roleType;
		this.restrs = restrs;
	}

	// F A C T O R Y

	public static VnRole parse(final String type, final String restrsXML) throws ParserConfigurationException, SAXException, IOException
	{
		final VnRoleType roleType = new VnRoleType(type);
		final VnRestrs restrs = restrsXML == null || restrsXML.isEmpty() || restrsXML.equals("<SELRESTRS/>") ? null : new VnRestrs(restrsXML, false);
		return new VnRole(roleType, restrs);
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
		final int cmp = this.roleType.compareTo(that.roleType);
		if (cmp != 0)
		{
			return cmp;
		}

		// same type
		if (this.restrs == null && that.restrs == null)
		{
			return 0;
		}

		// not both restrs null
		if (this.restrs == null)
		{
			return -1;
		}

		if (that.restrs == null)
		{
			return +1;
		}

		return this.restrs.compareTo(that.restrs);
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

	@Override
	public String dataRow()
	{
		// id
		// roleType.id
		// restrs.id (or null)
		return String.format("%d,%s", VnRoleType.MAP.get(roleType), restrs == null ? "NULL" : VnRestrs.MAP.get(restrs));
	}
}
