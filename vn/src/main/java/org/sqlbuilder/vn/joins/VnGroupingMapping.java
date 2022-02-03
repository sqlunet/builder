package org.sqlbuilder.vn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.vn.objects.VnClass;
import org.sqlbuilder.vn.objects.Grouping;
import org.sqlbuilder.vn.objects.VnWord;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class VnGroupingMapping implements Insertable, Comparable<VnGroupingMapping>
{
	public static final Set<VnGroupingMapping> SET = new HashSet<>();

	private final VnWord word;

	private final VnClass clazz;

	private final Grouping grouping;

	public VnGroupingMapping(final VnWord word, final VnClass clazz, final Grouping grouping)
	{
		this.word = word;
		this.clazz = clazz;
		this.grouping = grouping;
	}

	public VnWord getWord()
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

	// O R D E R I N G

	static public final Comparator<VnGroupingMapping> COMPARATOR = Comparator.comparing(VnGroupingMapping::getWord) //
			.thenComparing(VnGroupingMapping::getVnClass) //
			.thenComparing(VnGroupingMapping::getGrouping);

	@Override
	public int compareTo(final VnGroupingMapping that)
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
	@RequiresIdFrom(type = VnWord.class)
	@RequiresIdFrom(type = Grouping.class)
	@Override
	public String dataRow()
	{
		//	clazz.id
		//	word.id
		//	grouping.id
		return String.format("%d,%d,%d", VnClass.COLLECTOR.get(clazz), VnWord.COLLECTOR.get(word), Grouping.COLLECTOR.get(grouping));
	}
}
