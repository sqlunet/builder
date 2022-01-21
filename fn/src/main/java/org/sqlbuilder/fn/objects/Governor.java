package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.GovernorType;

public class Governor implements Insertable<Governor>
{
	public static final Set<Governor> SET = new HashSet<>();

	public final GovernorType governor;

	public final Word word;

	public Governor(final GovernorType governor)
	{
		this.governor = governor;
		this.word = new Word(governor.getLemma());
	}

	@Override
	public String dataRow()
	{
		return String.format("'%s','%s',%s", //
				Utils.escape(governor.getType()), //
				Utils.escape(word.word), //
				word.getId());
	}

	@Override
	public String toString()
	{
		return String.format("[GOV type=%s word=%s]", governor.getType(), word);
	}
}
