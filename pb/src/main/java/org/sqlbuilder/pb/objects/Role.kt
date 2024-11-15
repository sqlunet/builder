package org.sqlbuilder.pb.objects;

import org.sqlbuilder.annotations.RequiresIdFrom;
import org.sqlbuilder.common.*;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

public class Role implements HasId, Insertable, Comparable<Role>, Serializable
{
	public static final Comparator<Role> COMPARATOR = Comparator //
			.comparing(Role::getRoleSet) //
			.thenComparing(Role::getArgType) //
			.thenComparing(Role::getFunc, Comparator.nullsFirst(Comparator.naturalOrder())) //
			;

	public static final SetCollector<Role> COLLECTOR = new SetCollector<>(COMPARATOR);

	private final RoleSet roleSet;

	private final String argType;

	@Nullable
	private final Theta theta;

	@Nullable
	private final Func func;

	private final String descr;

	// C O N S T R U C T O R

	public static Role make(final RoleSet roleSet, final String n, final String f, final String descriptor, final String theta)
	{
		var r = new Role(roleSet, n, f, descriptor, theta);
		COLLECTOR.add(r);
		return r;
	}

	private Role(final RoleSet roleSet, final String argType, final String func, final String descriptor, final String theta)
	{
		this.roleSet = roleSet;
		this.argType = argType;
		this.theta = theta == null || theta.isEmpty() ? null : Theta.make(theta);
		this.func = func == null || func.isEmpty() ? null : Func.make(func);
		this.descr = descriptor;
	}

	// A C C E S S

	public RoleSet getRoleSet()
	{
		return this.roleSet;
	}

	public String getArgType()
	{
		return this.argType;
	}

	@Nullable
	public Func getFunc()
	{
		return func;
	}

	@Nullable
	public Theta getTheta()
	{
		return theta;
	}

	public String getDescr()
	{
		return this.descr;
	}

	@RequiresIdFrom(type = Role.class)
	@Override
	public Integer getIntId()
	{
		return COLLECTOR.get(this);
	}

	@RequiresIdFrom(type = Role.class)
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
		return roleSet.equals(that.roleSet) && argType.equals(that.argType) && Objects.equals(func, that.func);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(roleSet, argType, func);
	}

	// O R D E R I N G

	@Override
	public int compareTo(@NotNull final Role that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T

	@RequiresIdFrom(type = RoleSet.class)
	@RequiresIdFrom(type = Func.class)
	@RequiresIdFrom(type = Theta.class)
	@Override
	public String dataRow()
	{
		// (roleid),argtype,theta,func,roledescr,rolesetid
		return String.format("'%s',%s,%s,%s,%d", //
				argType, //
				Utils.nullable(theta, HasId::getSqlId), //
				Utils.nullable(func, HasId::getSqlId), //
				Utils.nullableQuotedEscapedString(descr), //
				roleSet.getIntId() //
		);
	}

	@Override
	public String comment()
	{
		return String.format("%s,%s,%s", roleSet.getName(), theta != null ? theta.getTheta() : "∅", func != null ? func.getFunc() : "∅");
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		if (this.descr == null)
		{
			return String.format("%s[%s-%s]", roleSet, argType, func);
		}
		return String.format("%s[%s-%s '%s']", roleSet, argType, func, descr);
	}
}
