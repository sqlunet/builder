package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.common.Utils;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import edu.berkeley.icsi.framenet.LexemeType;

public class Lexeme implements Insertable
{
	public static final Set<Lexeme> SET = new HashSet<>();

	private final int pos;

	private final boolean breakBefore;

	private final boolean headWord;

	private final int order;

	private final Word word;

	private final long luid;

	@SuppressWarnings("UnusedReturnValue")
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

	public static final Comparator<Lexeme> COMPARATOR = Comparator.comparing(Lexeme::getWord).thenComparing(Lexeme::getLuid);

	// I N S E R T

	@RequiresIdFrom(type = Word.class)
	@Override
	public String dataRow()
	{
		//fnwordid,posid,breakbefore,headword,lexemeidx,luid
		return String.format("%s,%d,%d,%d,%s,%d", //
				word.getSqlId(), // fnwordid
				pos, //
				breakBefore ? 1 : 0, //
				headWord ? 1 : 0, //
				Utils.zeroableInt(order), //
				luid);
	}

	@Override
	public String comment()
	{
		return String.format("word=%s", getWord());
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
