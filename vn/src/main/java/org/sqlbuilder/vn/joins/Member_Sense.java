package org.sqlbuilder.vn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.vn.objects.Sensekey;
import org.sqlbuilder.vn.objects.VnClass;
import org.sqlbuilder.vn.objects.VnWord;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Member_Sense implements Insertable, Comparable<Member_Sense>
{
	static public final Comparator<Member_Sense> COMPARATOR = Comparator.comparing(Member_Sense::getMember).thenComparing(Member_Sense::getSensekey);

	public static final Set<Member_Sense> SET = new HashSet<>();

	private final Class_Word member;

	private final int sensenum;

	private final Sensekey sensekey;

	private final Float quality;

	// C O N S T R U C T O R

	public static Member_Sense make(final Class_Word member, final int sensenum, final Sensekey sensekey, final Float quality)
	{
		var m = new Member_Sense(member, sensenum, sensekey, quality);
		SET.add(m);
		return m;
	}

	private Member_Sense(final Class_Word member, final int sensenum, final Sensekey sensekey, final Float quality)
	{
		this.member = member;
		this.sensenum = sensenum;
		this.sensekey = sensekey;
		this.quality = quality;
	}

	// A C C E S S

	public Class_Word getMember()
	{
		return member;
	}

	public Sensekey getSensekey()
	{
		return sensekey;
	}

	// O R D E R I N G

	@Override
	public int compareTo(final Member_Sense that)
	{
		return COMPARATOR.compare(this, that);
	}

	@Override
	public String toString()
	{
		return String.format("%s-%s-%s-%s", super.toString(), this.sensenum, this.sensekey, this.quality);
	}

	@RequiresIdFrom(type = VnClass.class)
	@RequiresIdFrom(type = VnWord.class)
	@Override
	// classid,wordid,sensenum,synsetid,sensekey,quality
	public String dataRow()
	{
		// class.id
		// word.id
		// sensenum
		// synsetId (or NULL)
		// sensekey (or NULL)
		// quality (or NULL)
		Integer synsetid = null;
		return String.format("%d,%d,%d,%s,%s,%s", //
				member.clazz.getIntId(), //
				member.word.getIntId(), //
				sensenum, //
				Utils.nullableInt(synsetid), //
				Utils.nullableQuotedString(sensekey, Sensekey::getSensekey), //
				Utils.nullableFloat(quality));
	}

	@Override
	public String comment()
	{
		return String.format("%s,%s", member.clazz.getName(), member.word.getWord());
	}
}
