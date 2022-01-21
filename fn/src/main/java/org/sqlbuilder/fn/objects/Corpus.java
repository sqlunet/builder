package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.HasID;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.CorpDocType;

public class Corpus implements HasID, Insertable<Corpus>
{
	public static final Set<Corpus> SET = new HashSet<>();

	public final CorpDocType corpus;

	public final Integer luid;

	public Corpus(final CorpDocType corpus, final Integer luid)
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
				Utils.nullableInt(luid));
	}

	@Override
	public String toString()
	{
		return String.format("[CORPUS id=%s name=%s]", corpus.getID(), corpus.getName());
	}
}
