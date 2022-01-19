package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.AnnotationSetType;

public class FnAnnotationSet implements Insertable<FnAnnotationSet>
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
		return 0;
		// return this.annoset.getID() + (this.fromFullText ? 100000000L : 0L);
	}

	public long getFrameId()
	{
		return 0;
		//return this.fromFullText ? this.annoset.getFrameID() : this.frame;
	}

	public long getLuId()
	{
		return 0;
		// return this.fromFullText ? this.annoset.getLuID() : this.lu;
	}

	@Override
	public String dataRow()
	{
		final long frameid2 = getFrameId();
		final long luid2 = getLuId();
		final long cxnid = this.annoset.getCxnID();

		// Long(1, getId());
		// Long(2, this.sentence);
		if (frameid2 != 0)
		{
			// Long(3, frameid2);
		}
		else
		{
			// Null(3, Types.INTEGER);
		}
		if (luid2 != 0)
		{
			// Long(4, luid2);
		}
		else
		{
			// Null(4, Types.INTEGER);
		}
		if (cxnid != 0)
		{
			// Long(5, cxnid);
		}
		else
		{
			// Null(5, Types.INTEGER);
		}
		// String(6, this.annoset.getCxnName());
		// String(7, this.annoset.getStatus());
		// String(8, this.annoset.getCDate());
		return null;
	}

	@Override
	public String toString()
	{
		return String.format("[AS frame=%s lu=%s]", getId(), getFrameId(), getLuId());
	}
}
