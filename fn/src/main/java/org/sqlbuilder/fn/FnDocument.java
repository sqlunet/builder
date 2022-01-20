package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.CorpDocType.Document;

public class FnDocument implements HasID, Insertable<FnDocument>
{
	public static final Set<FnDocument> SET = new HashSet<>();

	private final FnCorpus corpus;

	public final Document doc;

	public FnDocument(final FnCorpus corpus, final Document doc)
	{
		this.corpus = corpus;
		this.doc = doc;
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
