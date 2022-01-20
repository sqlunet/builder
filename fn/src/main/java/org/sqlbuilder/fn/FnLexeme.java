package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.objects.Word;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.LexemeType;

public class FnLexeme implements Insertable<FnLexeme>
{
	public static final Set<FnLexeme> SET = new HashSet<>();

	public final LexemeType lexeme;

	public final long luid;

	public final Word fnword;

	public FnLexeme(final long luid, final Word fnword, final LexemeType type)
	{
		this.luid = luid;
		this.fnword = fnword;
		this.lexeme = type;
	}

	@Override
	public String dataRow()
	{
		final int idx = this.lexeme.getOrder();

		// Long(1, getId());
		// Long(2, this.luid);
		// String(3, getWord());
		// Long(3, this.fnwordid);
		// Int(4, this.lexeme.getPOS().intValue());
		// Boolean(5, this.lexeme.getBreakBefore());
		// Boolean(6, this.lexeme.getHeadword());
		if (idx != 0)
		{
			// Int(7, idx);
		}
		else
		{
			// Null(7, Types.INTEGER);
		}
		return null;
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
		return String.format("[LEX luid=%s word=%s]", this.luid, getWord());
	}
}
