package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

public class FnWord implements Insertable<FnWord>
{
	public static final Set<FnWord> SET = new HashSet<>();

	public final String word;

	public FnWord(final String lemma)
	{
		this.word = lemma;
	}

	@Override
	public String dataRow()
	{
		// Long(1, getId());
		// Long(2, this.wordid.toLong());
		// Null(2, Types.INTEGER);
		// String(3, this.word);
		return null;
	}
}
