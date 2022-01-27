package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.Collector;
import org.sqlbuilder.fn.HasId;

import java.util.*;

/*
words.table=fnwords
words.create=CREATE TABLE IF NOT EXISTS %Fn_words.table% ( fnwordid INTEGER NOT NULL,wordid INTEGER NULL,word VARCHAR(30),PRIMARY KEY (fnwordid) );
words.pk=ALTER TABLE %Fn_words.table% ADD CONSTRAINT pk_%Fn_words.table% PRIMARY KEY (fnwordid);
words.no-pk=ALTER TABLE %Fn_words.table% DROP PRIMARY KEY;
words.unq1=CREATE UNIQUE INDEX IF NOT EXISTS unq_%Fn_words.table%_word ON %Fn_words.table% (word);
words.no-unq1=DROP INDEX IF EXISTS unq_%Fn_words.table%_word;
words.fk1=ALTER TABLE %Fn_words.table% ADD CONSTRAINT fk_%Fn_words.table%_wordid FOREIGN KEY (wordid) REFERENCES %Word.table% (wordid);
words.no-fk1=ALTER TABLE %Fn_words.table% DROP CONSTRAINT fk_%Fn_words.table%_wordid CASCADE;
words.insert=INSERT INTO %Fn_words.table% (fnwordid,wordid,word) VALUES(?,?,?);
words.select=SELECT fnwordid FROM %Fn_words.table% WHERE word = ?;
 */
public class Word implements HasId, Insertable<Word>
{
	public static Comparator<Word> COMPARATOR = Comparator.comparing(Word::getWord);

	public static final Collector<Word> COLLECTOR = new Collector<>(COMPARATOR);

	private final String word;

	public static Word make(final String lemma)
	{
		var w = new Word(lemma);
		Word.COLLECTOR.add(w);
		return w;
	}

	private Word(final String lemma)
	{
		this.word = lemma;
	}

	public String getWord()
	{
		return word;
	}

	@Override
	public Object getId()
	{
		Integer id = COLLECTOR.get(this);
		if (id != null)
		{
			return id;
		}
		return "NULL";
	}

	// I D E N T I T Y

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		Word word1 = (Word) o;
		return word.equals(word1.word);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(word);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("%s,'%s'", //
				"NULL", //
				Utils.escape(word));
	}

	@Override
	public String comment()
	{
		return String.format("id=%s", getId());
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return "W'" + word + '\'';
	}
}
