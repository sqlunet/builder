package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.common.HasID;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import edu.berkeley.icsi.framenet.SentenceType;

public class Sentence implements HasID, Insertable
{
	public static final Set<Sentence> SET = new HashSet<>();

	private final int sentenceid;

	private final Integer corpusid;

	private final Integer docid;

	private final int paragno;

	private final int sentno;

	private final int apos;

	private final String text;

	public static Sentence make(final SentenceType sentence)
	{
		var s = new Sentence(sentence);
		Sentence.SET.add(s);
		return s;
	}

	private Sentence(final SentenceType sentence)
	{
		this.sentenceid = sentence.getID();
		this.corpusid = sentence.getCorpID();
		this.docid = sentence.getDocID();
		this.paragno = sentence.getParagNo();
		this.sentno = sentence.getSentNo();
		this.text = sentence.getText();
		this.apos = sentence.getAPos();
	}

	// I D E N T I T Y

	public int getID()
	{
		return sentenceid;
	}

	public Integer getCorpusID()
	{
		return corpusid;
	}

	public Integer getDocID()
	{
		return docid;
	}

	// I D E N T I T Y

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		Sentence sentence = (Sentence) o;
		return sentenceid == sentence.sentenceid && Objects.equals(corpusid, sentence.corpusid) && Objects.equals(docid, sentence.docid);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(sentenceid, corpusid, docid);
	}

	// O R D E R

	public static final Comparator<Sentence> COMPARATOR = Comparator.comparing(Sentence::getID).thenComparing(Sentence::getDocID).thenComparing(Sentence::getCorpusID);

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("%d,%s,%s,%d,%d,'%s',%d", //
				sentenceid, //
				Utils.zeroableInt(corpusid), //
				Utils.zeroableInt(docid), //
				paragno, //
				sentno, //
				Utils.escape(text), //
				apos);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[SENT id=%s text=%s]", sentenceid, ellipsis(text));
	}

	private String ellipsis(String text)
	{
		int max = 32;
		int len = text.length();
		return text.substring(0, Math.min(max, len));
	}
}
