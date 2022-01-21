package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.LexemeType;

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
