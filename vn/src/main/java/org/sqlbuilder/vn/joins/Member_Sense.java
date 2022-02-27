package org.sqlbuilder.vn.joins;

import org.sqlbuilder.annotations.RequiresIdFrom;
import org.sqlbuilder.common.*;
import org.sqlbuilder.vn.objects.Sensekey;
import org.sqlbuilder.vn.objects.VnClass;
import org.sqlbuilder.vn.objects.Word;

import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class Member_Sense implements Insertable, Resolvable<String, SimpleEntry<Integer, Integer>>, Comparable<Member_Sense>
{
	static public final Comparator<Member_Sense> COMPARATOR = Comparator //
			.comparing(Member_Sense::getMemberClass) //
			.thenComparing(Member_Sense::getMemberWord) //
			.thenComparing(Member_Sense::getSensekey, Comparator.nullsFirst(Comparator.naturalOrder()));

	public static final Set<Member_Sense> SET = new HashSet<>();

	public static final Function<SimpleEntry<Integer, Integer>, String> RESOLVE_RESULT_STRINGIFIER = r -> //
			r == null ? "NULL,NULL" : String.format("%s,%s", Utils.nullableInt(r.getKey()), Utils.nullableInt(r.getValue()));
	//r == null ? "NULL,NULL" : String.format("%s,%s", r.first, r.second);

	private final Class_Word member;

	private final int sensenum;

	private final Sensekey sensekey;

	private final Float quality;

	// C O N S T R U C T O R

	@SuppressWarnings("UnusedReturnValue")
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

	public VnClass getMemberClass()
	{
		return member.clazz;
	}

	public Word getMemberWord()
	{
		return member.word;
	}

	public Sensekey getSensekey()
	{
		return sensekey;
	}

	// O R D E R I N G

	@Override
	public int compareTo(@NotNull final Member_Sense that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T

	@RequiresIdFrom(type = VnClass.class)
	@RequiresIdFrom(type = Word.class)
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

	// R E S O L V E

	@Override
	public String resolving()
	{
		return sensekey == null ? null : sensekey.getSensekey();
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("%s-%s-%s-%s", super.toString(), this.sensenum, this.sensekey, this.quality);
	}
}
