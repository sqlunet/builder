package org.sqlbuilder.vn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.NotNull;
import org.sqlbuilder.annotations.RequiresIdFrom;
import org.sqlbuilder.vn.objects.Grouping;
import org.sqlbuilder.vn.objects.VnClass;
import org.sqlbuilder.vn.objects.Word;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Member_Grouping implements Insertable, Comparable<Member_Grouping>
{
	static public final Comparator<Member_Grouping> COMPARATOR = Comparator.comparing(Member_Grouping::getWord) //
			.thenComparing(Member_Grouping::getVnClass) //
			.thenComparing(Member_Grouping::getGrouping);

	public static final Set<Member_Grouping> SET = new HashSet<>();

	private final Word word;

	private final VnClass clazz;

	private final Grouping grouping;

	// C O N S T R U C T O R

	@SuppressWarnings("UnusedReturnValue")
	public static Member_Grouping make(final VnClass clazz, final Word word, final Grouping grouping)
	{
		var m = new Member_Grouping(clazz, word, grouping);
		SET.add(m);
		return m;
	}

	private Member_Grouping(final VnClass clazz, final Word word, final Grouping grouping)
	{
		this.clazz = clazz;
		this.word = word;
		this.grouping = grouping;
	}

	// A C C E S S

	public Word getWord()
	{
		return word;
	}

	public VnClass getVnClass()
	{
		return clazz;
	}

	public Grouping getGrouping()
	{
		return grouping;
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
		Member_Grouping that = (Member_Grouping) o;
		return word.equals(that.word) && clazz.equals(that.clazz) && grouping.equals(that.grouping);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(word, clazz, grouping);
	}

	// O R D E R I N G

	@Override
	public int compareTo(@NotNull final Member_Grouping that)
	{
		return COMPARATOR.compare(this, that);
	}

	@Override
	public String toString()
	{
		return String.format("%s-%s-%s", this.word, this.clazz, this.grouping);
	}

	// I N S E R T

	@RequiresIdFrom(type = VnClass.class)
	@RequiresIdFrom(type = Word.class)
	@RequiresIdFrom(type = Grouping.class)
	@Override
	public String dataRow()
	{
		return String.format("%d,%d,%d", //
				clazz.getIntId(), //
				word.getIntId(), //
				grouping.getIntId());
	}

	@Override
	public String comment()
	{
		return String.format("%s,%s,%s", clazz.name, word.word, grouping.getName());
	}
}
