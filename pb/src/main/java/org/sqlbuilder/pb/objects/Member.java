package org.sqlbuilder.pb.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.NotNull;
import org.sqlbuilder.common.RequiresIdFrom;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Member implements Insertable, Comparable<Member>
{
	public static final Comparator<Member> COMPARATOR = Comparator //
			.comparing(Member::getWord) //
			.thenComparing(Member::getRoleSet);

	public static final Set<Member> SET = new HashSet<>();

	final RoleSet roleSet;

	final Word word;

	// C O N S T R U C T O R

	@SuppressWarnings("UnusedReturnValue")
	public static Member make(final RoleSet roleSet, final Word word)
	{
		var m = new Member(roleSet, word);
		SET.add(m);
		return m;
	}

	private Member(final RoleSet roleSet, final Word word)
	{
		this.roleSet = roleSet;
		this.word = word;
		SET.add(this);
	}

	// A C C E S S

	public RoleSet getRoleSet()
	{
		return roleSet;
	}

	// I D E N T I T Y

	public Word getWord()
	{
		return word;
	}

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
		Member that = (Member) o;
		return roleSet.equals(that.roleSet) && word.equals(that.word);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(roleSet, word);
	}

	// O R D E R

	@Override
	public int compareTo(@NotNull final Member that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T

	@RequiresIdFrom(type = RoleSet.class)
	@RequiresIdFrom(type = Word.class)
	@Override
	public String dataRow()
	{
		return String.format("%s,%s", roleSet.getIntId(), word.getIntId());
	}

	@Override
	public String comment()
	{
		return String.format("%s,%s", roleSet.getName(), word.getWord());
	}
}
