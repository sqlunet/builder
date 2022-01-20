package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.objects.Word;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.GovernorType;

public class FnGovernor implements Insertable<FnGovernor>
{
	public static final Set<FnGovernor> SET = new HashSet<>();

	public final Word fnword;

	public final GovernorType governor;

	public FnGovernor(final Word word, final GovernorType governor)
	{
		this.fnword = word;
		this.governor = governor;
	}

	@Override
	public String dataRow()
	{
		return String.format("'%s', NULL, '%s'", //
				Utils.escape(governor.getLemma()), //
				Utils.escape(governor.getType())
				);
	}

	@Override
	public String toString()
	{
		return String.format("[GOV lemma=%s type=%s]", fnword, governor.getType());
	}
}
