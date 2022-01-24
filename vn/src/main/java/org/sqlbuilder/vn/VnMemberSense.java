package org.sqlbuilder.vn;

import org.sqlbuilder.common.Insertable;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class VnMemberSense implements Insertable<VnMemberSense>, Comparable<VnMemberSense>
{
	protected static final Set<VnMemberSense> SET = new HashSet<>();

	private final VnMember member;

	private final int sensenum;

	private final VnSensekey sensekey;

	private final Float quality;

	public VnMemberSense(final VnMember member, final int sensenum, final VnSensekey sensekey, final Float quality)
	{
		this.member = member;
		this.sensenum = sensenum;
		this.sensekey = sensekey;
		this.quality = quality;
	}

	public VnMember getMember()
	{
		return member;
	}

	public VnSensekey getSensekey()
	{
		return sensekey;
	}

	// O R D E R I N G

	static public final Comparator<VnMemberSense> COMPARATOR = Comparator.comparing(VnMemberSense::getMember).thenComparing(VnMemberSense::getSensekey);

	@Override
	public int compareTo(final VnMemberSense that)
	{
		return COMPARATOR.compare(this, that);
	}

	@Override
	public String toString()
	{
		return String.format("%s-%s-%s-%s", super.toString(), this.sensenum, this.sensekey, this.quality);
	}

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
		Long synsetid = null;
		return String.format("%d,%d,%d,%s,%s,%s", //
				VnClass.MAP.get(member.clazz), //
				VnWord.MAP.get(member.word), //
				sensenum, //
				synsetid != null ? synsetid : "NULL", //
				sensekey != null ? "'" + sensekey.getSensekey() + "'" : "NULL", //
				quality != null ? quality : "NULL");
	}
}
