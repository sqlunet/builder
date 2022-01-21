package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.HasID;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.CorpDocType;
import edu.berkeley.icsi.framenet.CorpDocType.Document;

public class Doc implements HasID, Insertable<Doc>
{
	public static final Set<Doc> SET = new HashSet<>();

	public final Document doc;

	private final CorpDocType corpus;

	public Doc(final Document doc, final CorpDocType corpus)
	{
		this.doc = doc;
		this.corpus = corpus;
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
