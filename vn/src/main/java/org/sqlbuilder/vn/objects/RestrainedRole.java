package org.sqlbuilder.vn.objects;

import org.sqlbuilder.common.*;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

public class RestrainedRole implements Comparable<RestrainedRole>
{
	public static final Comparator<RestrainedRole> COMPARATOR = Comparator.comparing(RestrainedRole::getRoleType).thenComparing(RestrainedRole::getRestrs, Comparator.nullsFirst(Comparator.naturalOrder()));

	public static final Set<RestrainedRole> SET = new HashSet<>();

	private final RoleType roleType;

	private final Restrs restrs;

	// C O N S T R U C T O R
	public static RestrainedRole make(final String type, final String restrsXML) throws ParserConfigurationException, SAXException, IOException
	{
		final RoleType roleType = RoleType.make(type);
		final Restrs restrs = restrsXML == null || restrsXML.isEmpty() || restrsXML.equals("<SELRESTRS/>") ? null : Restrs.make(restrsXML, false);
		var r = new RestrainedRole(roleType, restrs);
		SET.add(r);
		return r;
	}

	private RestrainedRole(final RoleType roleType, final Restrs restrs)
	{
		this.roleType = roleType;
		this.restrs = restrs;
	}

	// A C C E S S

	public RoleType getRoleType()
	{
		return this.roleType;
	}

	public Restrs getRestrs()
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
		RestrainedRole that = (RestrainedRole) o;
		return roleType.equals(that.roleType) && Objects.equals(restrs, that.restrs);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(roleType, restrs);
	}

	// O R D E R I N G

	@Override
	public int compareTo(final RestrainedRole that)
	{
		return COMPARATOR.compare(this, that);
	}

	/*
	// I N S E R T

	@RequiresIdFrom(type = RoleType.class)
	@RequiresIdFrom(type = Restrs.class)
	public String dataRow()
	{
		// id
		// roleType.id
		// restrs.id (or null)
		return String.format("%d,%s", //
				roleType.getIntId(), //
				Utils.nullable(restrs, HasId::getSqlId));
	}

	public String comment()
	{
		return String.format("%s", roleType.getType());
	}
	*/

	// T O S T R I N G

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
}
