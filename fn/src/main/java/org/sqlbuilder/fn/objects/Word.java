package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.HasId;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Word implements HasId, Insertable<Word>
{
	public static final Set<Word> SET = new HashSet<>();

	public static Map<Word, Integer> MAP;

	public final String word;

	public Word(final String lemma)
	{
		this.word = lemma;
		Word.SET.add(this);
	}

	public String getWord()
	{
		return word;
	}

	public static Comparator<Word> COMPARATOR = Comparator.comparing(Word::getWord);

	@Override
	public Object getId()
	{
		Integer id = MAP.get(this);
		if (id != null)
		{
			return id;
		}
		return "NULL";
	}

	@Override
	public String dataRow()
	{
		return String.format("%s,%s,'%s'", //
				"NULL", //
				"NULL", //
				Utils.escape(word));
	}

	@Override
	public String toString()
	{
		return "W'" + word + '\'';
	}
}
