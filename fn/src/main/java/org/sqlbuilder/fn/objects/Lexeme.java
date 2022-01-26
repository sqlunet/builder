package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;

import java.util.*;

import edu.berkeley.icsi.framenet.LexemeType;

/*
lexemes.table=fnlexemes
lexemes.create=CREATE TABLE IF NOT EXISTS %Fn_lexemes.table% ( lexemeid INTEGER NOT NULL,luid INTEGER,fnwordid INTEGER,posid INTEGER,breakbefore BOOLEAN,headword BOOLEAN,lexemeidx INTEGER DEFAULT NULL,PRIMARY KEY (lexemeid) );
lexemes.index1=CREATE INDEX IF NOT EXISTS k_%Fn_lexemes.table%_luid ON %Fn_lexemes.table% (luid);
lexemes.no-index1=DROP INDEX IF EXISTS k_%Fn_lexemes.table%_luid;
lexemes.index2=CREATE INDEX IF NOT EXISTS k_%Fn_lexemes.table%_fnwordid ON %Fn_lexemes.table% (fnwordid);
lexemes.no-index2=DROP INDEX IF EXISTS k_%Fn_lexemes.table%_fnwordid;
lexemes.fk1=ALTER TABLE %Fn_lexemes.table% ADD CONSTRAINT fk_%Fn_lexemes.table%_luid FOREIGN KEY (luid) REFERENCES %Fn_lexunits.table% (luid);
lexemes.fk2=ALTER TABLE %Fn_lexemes.table% ADD CONSTRAINT fk_%Fn_lexemes.table%_posid FOREIGN KEY (posid) REFERENCES %Fn_poses.table% (posid);
lexemes.fk3=ALTER TABLE %Fn_lexemes.table% ADD CONSTRAINT fk_%Fn_lexemes.table%_fnwordid FOREIGN KEY (fnwordid) REFERENCES %Fn_words.table% (fnwordid);
lexemes.no-fk1=ALTER TABLE %Fn_lexemes.table% DROP CONSTRAINT fk_%Fn_lexemes.table%_luid CASCADE;
lexemes.no-fk2=ALTER TABLE %Fn_lexemes.table% DROP CONSTRAINT fk_%Fn_lexemes.table%_posid CASCADE;
lexemes.no-fk3=ALTER TABLE %Fn_lexemes.table% DROP CONSTRAINT fk_%Fn_lexemes.table%_fnwordid CASCADE;
lexemes.insert=INSERT INTO %Fn_lexemes.table% (lexemeid,luid,fnwordid,posid,breakbefore,headword,lexemeidx) VALUES(?,?,?,?,?,?,?);
 */
public class Lexeme implements Insertable<Lexeme>
{
	public static final Set<Lexeme> SET = new HashSet<>();

	private final int pos;

	private final boolean breakBefore;

	private final boolean headWord;

	private final int order;

	private final Word word;

	private final long luid;

	public static Lexeme make(final LexemeType lexeme, final long luid)
	{
		var l = new Lexeme(lexeme, luid);
		SET.add(l);
		return l;
	}

	private Lexeme(final LexemeType lexeme, final long luid)
	{
		this.word = Word.make(trim(lexeme.getName()));
		this.pos = lexeme.getPOS().intValue();
		this.breakBefore = lexeme.getBreakBefore();
		this.headWord = lexeme.getHeadword();
		this.order = lexeme.getOrder();
		this.luid = luid;
	}

	// A C C E S S

	public String getWord()
	{
		return this.word.getWord();
	}

	public long getLuid()
	{
		return luid;
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
		Lexeme that = (Lexeme) o;
		return word.equals(that.word) && luid == that.luid;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(word, luid);
	}

	// O R D E R

	public static Comparator<Lexeme> COMPARATOR = Comparator.comparing(Lexeme::getWord).thenComparing(Lexeme::getLuid);

	// I N S E R T

	@Override
	public String dataRow()
	{
		//fnwordid,posid,breakbefore,headword,lexemeidx,luid
		return String.format("%s,%d,%d,%d,%s,%d", //
				word.getId(), // fnwordid
				pos, //
				breakBefore ? 1 : 0, //
				headWord ? 1 : 0, //
				Utils.zeroableInt(order), //
				luid);
	}

	@Override
	public String comment()
	{
		return String.format("%s", getWord());
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[LEX word=%s luid=%s]", getWord(), luid);
	}

	// W O R D
	/* frame
	name="construction(entity)"
	name="power_((statistical))"
	name="talk_(to)"
	name="Indian((American))"
	name="practice_((mass))"
	name="rehearsal_((mass))"
	name="late_((at_night))"
	*/
	/* lexunit
	name="practice_((mass))"
	name="rehearsal_((mass))"
	name="Indian((American))"
	name="construction(entity)"
	name="talk_(to)"
	name="power_((statistical))"
	name="late_((at_night))"
	*/
	private static String trim(final String string)
	{
		return string.replaceAll("_*\\(.*$", "");
	}
}
