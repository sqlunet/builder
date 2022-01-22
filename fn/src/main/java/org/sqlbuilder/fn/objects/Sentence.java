package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.FnModule;
import org.sqlbuilder.fn.HasID;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.SentenceType;

/*
sentences.table=fnsentences
sentences.create=CREATE TABLE IF NOT EXISTS %Fn_sentences.table% ( sentenceid INTEGER NOT NULL,documentid INTEGER DEFAULT NULL,corpusid INTEGER DEFAULT NULL,paragno INTEGER,sentno INTEGER,text TEXT,apos INTEGER,noccurs INTEGER DEFAULT 1,PRIMARY KEY (sentenceid) );
sentences.fk1=ALTER TABLE %Fn_sentences.table% ADD CONSTRAINT fk_%Fn_sentences.table%_documentid FOREIGN KEY (documentid) REFERENCES %Fn_documents.table% (documentid);
sentences.fk2=ALTER TABLE %Fn_sentences.table% ADD CONSTRAINT fk_%Fn_sentences.table%_corpusid FOREIGN KEY (corpusid) REFERENCES %Fn_corpuses.table% (corpusid);
sentences.no-fk1=ALTER TABLE %Fn_sentences.table% DROP CONSTRAINT fk_%Fn_sentences.table%_corpusid CASCADE;
sentences.no-fk2=ALTER TABLE %Fn_sentences.table% DROP CONSTRAINT fk_%Fn_sentences.table%_documentid CASCADE;
sentences.insert=INSERT INTO %Fn_sentences.table% (sentenceid,corpusid,documentid,paragno,sentno,text,apos) VALUES(?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE noccurs=noccurs+1;
 */
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
		final boolean isNew = Sentence.SET.add(this);
		if (!isNew)
		{
			Logger.instance.logWarn(FnModule.MODULE_ID, "Sentence", "sentence-duplicate", null, -1, null, sentence.toString());
		}
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
