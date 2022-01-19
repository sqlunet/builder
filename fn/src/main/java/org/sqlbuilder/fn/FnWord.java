package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FnWord implements Insertable<FnWord>
{
	public static final Set<FnWord> SET = new HashSet<>();

	public static Map<FnWord, Integer> MAP;

	public final String word;

	public FnWord(final String lemma)
	{
		this.word = lemma;
	}

	public String getWord()
	{
		return word;
	}

	public static Comparator<FnWord> COMPARATOR = Comparator.comparing(FnWord::getWord);

	@Override
	public String dataRow()
	{
		// Long(1, getId());
		// Long(2, this.wordid.toLong());
		// Null(2, Types.INTEGER);
		// String(3, this.word);
		return String.format("%s,'%s'", "NULL", Utils.escape(word));
	}
}
