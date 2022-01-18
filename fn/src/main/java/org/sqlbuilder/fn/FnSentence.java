package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.SentenceType;

public class FnSentence implements Insertable<FnSentence>
{
	public static final Set<FnSentence> SET = new HashSet<>();

	public final SentenceType sentence;

	private final boolean fromFullText;

	public FnSentence(final SentenceType sentence, final boolean fromFullText)
	{
		super();
		this.sentence = sentence;
		this.fromFullText = fromFullText;
	}

	public long getId()
	{
		return this.sentence.getID() + (this.fromFullText ? 100000000L : 0L);
	}

	@Override
	public String dataRow()
	{
		final int corpusid = this.sentence.getCorpID();
		final int documentid = this.sentence.getDocID();
		final long sentenceid = getId();

		// Long(1, sentenceid);
		if (corpusid != 0)
		{
			// Int(2, corpusid);
		}
		else
		{
			// Null(2, Types.INTEGER);
		}
		if (documentid != 0)
		{
			// Int(3, documentid);
		}
		else
		{
			// Null(3, Types.INTEGER);
		}
		// Int(4, this.sentence.getParagNo());
		// Int(5, this.sentence.getSentNo());
		// String(6, this.sentence.getText());
		// Int(7, this.sentence.getAPos());
		return null;
	}

	@Override
	public String toString()
	{
		return String.format("[SENT sentenceid=%s id=%s corpusid=%s docid=%s]", getId(), this.sentence.getID(), this.sentence.getCorpID(), this.sentence.getDocID());
	}
}
