package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.CorpDocType;

public class FnCorpus implements HasID, Insertable<FnCorpus>
{
	public static final Set<FnCorpus> SET = new HashSet<>();

	public final CorpDocType corpus;

	public final Long luid;

	public FnCorpus(final CorpDocType corpus, final Long luid)
	{
		this.corpus = corpus;
		this.luid = luid;
	}

	@Override
	public String dataRow()
	{
		return String.format("%d,'%s','%s',%s", //
				corpus.getID(), //
				corpus.getName(), //
				Utils.escape(corpus.getDescription()), //
				Utils.nullableLong(luid));
	}

	@Override
	public String toString()
	{
		return String.format("[CORPUS corpus=%s name=%s]", this.corpus, this.corpus.getName());
	}
}
