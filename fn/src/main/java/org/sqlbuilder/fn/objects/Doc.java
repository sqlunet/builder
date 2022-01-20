package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.HasID;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.CorpDocType.Document;

public class Doc implements HasID, Insertable<Doc>
{
	public static final Set<Doc> SET = new HashSet<>();

	public final Document doc;

	private final Corpus corpus;

	public Doc(final Document doc, final Corpus corpus)
	{
		this.doc = doc;
		this.corpus = corpus;
	}

	@Override
	public String dataRow()
	{
		// Long(1, this.doc.getID());
		// Long(2, this.corpusid);
		// String(3, this.doc.getDescription());
		return null;
	}

	@Override
	public String toString()
	{
		return String.format("[DOC corpus=%s description=%s]", this.corpus, this.doc);
	}
}
