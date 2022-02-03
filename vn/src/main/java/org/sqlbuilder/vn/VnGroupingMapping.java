package org.sqlbuilder.vn;

import org.sqlbuilder.common.Insertable;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class VnGroupingMapping implements Insertable, Comparable<VnGroupingMapping>
{
	protected static final Set<VnGroupingMapping> SET = new HashSet<>();

	private final VnWord word;

	private final VnClass clazz;

	private final VnGrouping grouping;

	public VnGroupingMapping(final VnWord word, final VnClass clazz, final VnGrouping grouping)
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

	public VnGrouping getGrouping()
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

	@Override
	public String dataRow()
	{
		//	clazz.id
		//	word.id
		//	grouping.id
		return String.format("%d,%d,%d", VnClass.MAP.get(clazz), VnWord.MAP.get(word), VnGrouping.MAP.get(grouping));
	}
}
