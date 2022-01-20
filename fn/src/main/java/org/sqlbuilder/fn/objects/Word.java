package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Word implements Insertable<Word>
{
	public static final Set<Word> SET = new HashSet<>();

	public static Map<Word, Integer> MAP;

	public final String word;

	public Word(final String lemma)
	{
		this.word = lemma;
	}

	public String getWord()
	{
		return word;
	}

	public static Comparator<Word> COMPARATOR = Comparator.comparing(Word::getWord);

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
