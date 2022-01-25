package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.HasID;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
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

	private final int docid;

	private final String name;

	private final String description;

	private final int corpusid;

	public static void make(final Document doc, final CorpDocType corpus)
	{
		var d = new Doc(doc, corpus);
		SET.add(d);
	}

	private Doc(final Document doc, final CorpDocType corpus)
	{
		this.docid = doc.getID();
		this.name = doc.getName();
		this.description = doc.getDescription();
		this.corpusid = corpus.getID();
	}

	// A C C E S S

	public int getID()
	{
		return docid;
	}

	public String getName()
	{
		return name;
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
		Doc that = (Doc) o;
		return docid == that.docid;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(docid);
	}

	// O R D E R

	public static final Comparator<Doc> COMPARATOR = Comparator.comparing(Doc::getName).thenComparing(Doc::getID);

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("%d,'%s',%d", //
				docid, //
				Utils.escape(description), //
				corpusid);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[DOC id=%s corpusid=%s]", docid, corpusid);
	}
}
