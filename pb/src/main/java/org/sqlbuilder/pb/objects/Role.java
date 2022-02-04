package org.sqlbuilder.pb.objects;

import org.sqlbuilder.common.*;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

public class Role implements HasId, Insertable, Comparable<Role>, Serializable
{
	public static final Comparator<Role> COMPARATOR = Comparator //
			.comparing(Role::getRoleSet) //
			.thenComparing(Role::getArgN) //
			.thenComparing(Role::getF, Comparator.nullsFirst(Comparator.naturalOrder())) //
			.thenComparing(Role::getTheta, Comparator.nullsFirst(Comparator.naturalOrder()));

	public static final SetCollector<Role> COLLECTOR = new SetCollector<>(COMPARATOR);

	private final RoleSet roleSet;

	private final String argN;

	private final Func f;

	private final Theta theta;

	private final String descr;

	// C O N S T R U C T O R

	public static Role make(final RoleSet roleSet, final String n, final String f, final String theta, final String descriptor)
	{
		var r = new Role(roleSet, n, f, theta, descriptor);
		COLLECTOR.add(r);
		return r;
	}

	private Role(final RoleSet roleSet, final String n, final String f, final String theta, final String descriptor)
	{
		this.roleSet = roleSet;
		this.argN = n;
		this.f = f == null || f.isEmpty() ? null : Func.make(f);
		this.theta = theta == null || theta.isEmpty() ? null : Theta.make(theta);
		this.descr = descriptor;
	}

	// A C C E S S

	public RoleSet getRoleSet()
	{
		return this.roleSet;
	}

	public String getArgN()
	{
		return this.argN;
	}

	public Func getF()
	{
		return f;
	}

	public Theta getTheta()
	{
		return theta;
	}

	public String getDescr()
	{
		return this.descr;
	}

	@RequiresIdFrom(type = Theta.class)
	@Override
	public Integer getIntId()
	{
		return COLLECTOR.get(this);
	}

	@RequiresIdFrom(type=Theta.class)
	public static Integer getIntId(final Role role)
	{
		return role == null ? null : COLLECTOR.get(role);
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
		Role that = (Role) o;
		return roleSet.equals(that.roleSet) && argN.equals(that.argN) && Objects.equals(f, that.f) && Objects.equals(theta, that.theta);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(roleSet, argN, f, theta);
	}

	// O R D E R I N G

	@Override
	public int compareTo(final Role that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T

	@RequiresIdFrom(type=RoleSet.class)
	@RequiresIdFrom(type=Func.class)
	@RequiresIdFrom(type=Theta.class)
	@Override
	public String dataRow()
	{
		// (roleid),narg,func,theta,roledescr,rolesetid
		// final long roleId = Role.COLLECTOR.get(this);
		return String.format("'%s',%s,%s,'%s',%d", //
				argN, //
				f == null ? "NULL" : f.getSqlId(), //
				theta == null ? "NULL" : theta.getSqlId(), //
				descr, //
				roleSet.getIntId() //
		);
	}

	@Override
	public String comment()
	{
		return String.format("%s,%s,%s", roleSet.getName(), f, theta);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		if (this.descr == null)
		{
			return String.format("%s[%s]", this.roleSet, this.argN);
		}
		return String.format("%s[%s-{%s}]", this.roleSet, this.argN, this.descr);
	}
}
