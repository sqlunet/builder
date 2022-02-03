package org.sqlbuilder.vn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.RequiresIdFrom;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class VnMember implements Insertable, Comparable<VnMember>
{
	public static final Set<VnMember> SET = new HashSet<>();

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

	@RequiresIdFrom(type = VnClass.class)
	@RequiresIdFrom(type = VnWord.class)
	@Override
	public String dataRow()
	{
		// class.id
		// word.id
		return String.format("%d,%d", VnClass.COLLECTOR.get(clazz), VnWord.MAP.get(word));
	}
}
