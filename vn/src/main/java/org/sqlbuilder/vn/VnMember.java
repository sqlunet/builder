package org.sqlbuilder.vn;

import org.sqlbuilder.common.Insertable;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class VnMember implements Insertable<VnMember>, Comparable<VnMember>
{
	protected static final SortedSet<VnMember> SET = new TreeSet<>();

	protected final VnWord word;

	protected final VnClass clazz;

	public VnMember(final VnClass vnclass, final VnWord word)
	{
		super();
		this.clazz = vnclass;
		this.word = word;
	}

	public VnWord getWord()
	{
		return word;
	}

	public VnClass getClazz()
	{
		return clazz;
	}

	// O R D E R I N G

	static public final Comparator<VnMember> COMPARATOR = Comparator.comparing(VnMember::getWord).thenComparing(VnMember::getClazz);

	@Override
	public int compareTo(final VnMember that)
	{
		return COMPARATOR.compare(this, that);
	}

	@Override
	public String toString()
	{
		return String.format("%s-%s", this.clazz, this.word);
	}

	@Override
	public String dataRow()
	{
		// class.id
		// word.id
		return String.format("%d,%d", VnClass.MAP.get(clazz), VnWord.MAP.get(word));
	}
}
