package org.sqlbuilder.fn.joins;

import org.sqlbuilder.fn.objects.Governor;

import java.util.HashSet;
import java.util.Set;

public class LexUnit_Governor extends Pair<Long, Governor>
{
	public static final Set<LexUnit_Governor> SET = new HashSet<>();

	public LexUnit_Governor(final long luid, final Governor governor)
	{
		super(luid, governor);
	}

	@Override
	public String toString()
	{
		return String.format("[LU-GOV lu=%s governor=%s]", this.first, this.second);
	}
}
