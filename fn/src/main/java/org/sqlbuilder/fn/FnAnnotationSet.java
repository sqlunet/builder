package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.AnnotationSetType;

public class FnAnnotationSet implements HasID, Insertable<FnAnnotationSet>
{
	public static final Set<FnAnnotationSet> SET = new HashSet<>();

	public final AnnotationSetType annoset;

	public final long sentenceid;

	public final Long frameid;

	public final Long luid;

	private final boolean fromFullText;

	public FnAnnotationSet(final long sentenceid, final AnnotationSetType annoset)
	{
		this(sentenceid, annoset, null, null,true);
	}

	public FnAnnotationSet(final long sentenceid, final AnnotationSetType annoset, final long luid, final long frameid)
	{
		this(sentenceid, annoset, luid, frameid, false);
	}

	public FnAnnotationSet(final long sentenceid, final AnnotationSetType annoset, final Long luid, final Long frameid, final boolean fromFullText)
	{
		super();
		this.annoset = annoset;
		this.sentenceid = sentenceid;
		this.luid = luid;
		this.frameid = frameid;
		this.fromFullText = fromFullText;
	}

	public long getId()
	{
		return annoset.getID() + (fromFullText ? 100000000L : 0L);
	}

	public long getFrameId()
	{
		return fromFullText ? annoset.getFrameID() : frameid;
	}

	public long getLuId()
	{
		return this.fromFullText ? annoset.getLuID() : luid;
	}

	@Override
	public String dataRow()
	{
		final long frameid2 = getFrameId();
		final long luid2 = getLuId();
		final long cxnid2 = annoset.getCxnID();

		return String.format("%d,%d,%s,%s,%s", //
				getId(), //
				sentenceid, //
				Utils.zeroableLong(frameid2), //
				Utils.zeroableLong(luid2), //
				Utils.zeroableLong(cxnid2) //
				);
		// String(6, this.annoset.getCxnName());
		// String(7, this.annoset.getStatus());
		// String(8, this.annoset.getCDate());
	}

	@Override
	public String toString()
	{
		return String.format("[AS id=%s frameid=%s luid=%s]", getId(), getFrameId(), getLuId());
	}
}
