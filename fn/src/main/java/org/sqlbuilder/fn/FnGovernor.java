package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.GovernorType;

public class FnGovernor implements Insertable<FnGovernor>
{
	public static final Set<FnGovernor> SET = new HashSet<>();

	public final FnWord fnword;

	public final GovernorType governor;

	public FnGovernor(final FnWord word, final GovernorType governor)
	{
		this.fnword = word;
		this.governor = governor;
	}

	@Override
	public String dataRow()
	{
		// Long(1, getId());
		// // String(2, this.governor.getLemma());
		// Long(2, this.fnwordid);
		// String(3, this.governor.getType());
		return null;
	}

	@Override
	public String toString()
	{
		return String.format("[GOV lemma=%s type=%s]", fnword, governor.getType());
	}
}
