package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.HasID;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.CorpDocType;
import edu.berkeley.icsi.framenet.CorpDocType.Document;

/*
documents.table=fndocuments
documents.create=CREATE TABLE IF NOT EXISTS %Fn_documents.table% ( documentid INTEGER NOT NULL,corpusid INTEGER DEFAULT NULL,documentdesc VARCHAR(84),noccurs INTEGER DEFAULT 1,PRIMARY KEY (documentid) );
documents.fk1=ALTER TABLE %Fn_documents.table% ADD CONSTRAINT fk_%Fn_documents.table%_corpusid FOREIGN KEY (corpusid) REFERENCES %Fn_corpuses.table% (corpusid);
documents.no-fk1=ALTER TABLE %Fn_documents.table% DROP CONSTRAINT fk_%Fn_documents.table%_corpusid CASCADE;
documents.insert=INSERT INTO %Fn_documents.table% (documentid,corpusid,documentdesc) VALUES(?,?,?) ON DUPLICATE KEY UPDATE noccurs=noccurs+1;
 */
public class Doc implements HasID, Insertable<Doc>
{
	public static final Set<Doc> SET = new HashSet<>();

	public final Document doc;

	private final CorpDocType corpus;

	public Doc(final Document doc, final CorpDocType corpus)
	{
		this.doc = doc;
		this.corpus = corpus;
		SET.add(this);
	}

	@Override
	public String dataRow()
	{
		return String.format("%d,'%s',%d", //
				doc.getID(), //
				Utils.escape(doc.getDescription()), //
				corpus.getID());
	}

	@Override
	public String toString()
	{
		return String.format("[DOC id=%s corpusid=%s]", doc.getID(), corpus.getID());
	}
}
