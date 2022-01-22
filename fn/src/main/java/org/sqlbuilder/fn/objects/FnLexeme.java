package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;

import java.util.HashSet;
import java.util.Set;

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
public class FnLexeme implements Insertable<FnLexeme>
{
	public static final Set<FnLexeme> SET = new HashSet<>();

	public final LexemeType lexeme;

	public final Word word;

	public final long luid;

	public FnLexeme(final LexemeType type, final Word fnword, final long luid)
	{
		this.lexeme = type;
		this.word = fnword;
		this.luid = luid;
		SET.add(this);
	}

	@Override
	public String dataRow()
	{
		final int idx = this.lexeme.getOrder();

		return String.format("%s,'%s',%s,%d,%b,%b,%s,%d", //
				"NULL", // getId()
				Utils.escape(getWord()), //
				"NULL", // fnwordid
				lexeme.getPOS().intValue(), //
				lexeme.getBreakBefore(), //
				lexeme.getHeadword(), //
				Utils.zeroableInt(idx), //
				luid);
	}

	public String getWord()
	{
		return FnLexeme.makeWord(this.lexeme.getName());
	}

	public static String makeWord(final String string)
	{
		return string.replaceAll("_*\\(.*$", "");
	}

	@Override
	public String toString()
	{
		return String.format("[LEX word=%s luid=%s]", getWord(), luid);
	}
}
