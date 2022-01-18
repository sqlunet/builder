package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.AnnotationSetType;

public class FnAnnotationSet implements Insertable<FnAnnotationSet>
{
	public static final Set<FnAnnotationSet> SET = new HashSet<>();

	public final AnnotationSetType annoset;

	public final FnSentence sentence;

	public final FnLexUnit lu;

	private final boolean fromFullText;

	public FnAnnotationSet(final FnSentence sentence, final AnnotationSetType annoset)
	{
		super();
		this.annoset = annoset;
		this.sentence = sentence;
		this.lu = null;
		this.fromFullText = true;
	}

	public FnAnnotationSet(final FnSentence sentence, final AnnotationSetType annoset, final FnLexUnit lu)
	{
		super();
		this.annoset = annoset;
		this.sentence = sentence;
		this.lu = lu;
		this.fromFullText = false;
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
