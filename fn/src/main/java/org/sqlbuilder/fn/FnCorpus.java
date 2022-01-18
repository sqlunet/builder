package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.CorpDocType;

public class FnCorpus implements Insertable<FnCorpus>
{
	public static final Set<FnCorpus> SET = new HashSet<>();

	public final CorpDocType corpus;

	public final FnLexUnit lu;

	public FnCorpus(final CorpDocType corpus, final FnLexUnit lu)
	{
		this.corpus = corpus;
		this.lu = lu;
	}

	@Override
	public String dataRow()
	{
		// Long(1, this.corpus.getID());
		// String(2, this.corpus.getName());
		// String(3, this.corpus.getDescription());
		if (this.lu != null)
		{
			// Long(4, this.luid);
		}
		else
		{
			// Null(4, Types.INTEGER);
		}
		return null;
	}

	@Override
	public String toString()
	{
		return String.format("[CORPUS corpus=%s name=%s]", this.corpus, this.corpus.getName());
	}
}
