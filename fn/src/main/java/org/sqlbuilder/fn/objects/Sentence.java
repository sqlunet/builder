package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.HasID;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.SentenceType;

public class Sentence implements HasID, Insertable<Sentence>
{
	public static final Set<Sentence> SET = new HashSet<>();

	public final SentenceType sentence;

	private final boolean fromFullText;

	public Sentence(final SentenceType sentence, final boolean fromFullText)
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
		return String.format("%d,%s,%s,", //
				sentence.getID(), //
				Utils.zeroableLong(sentence.getCorpID()), //
				Utils.zeroableLong(sentence.getDocID()), //
				sentence.getParagNo(), //
				sentence.getSentNo(), //
				sentence.getText(), //
				sentence.getAPos());
	}

	@Override
	public String toString()
	{
		return String.format("[SENT id=%s id=%s corpusid=%s docid=%s]", getId(), this.sentence.getID(), this.sentence.getCorpID(), this.sentence.getDocID());
	}
}
