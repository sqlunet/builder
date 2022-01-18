package org.sqlbuilder.fn;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.SemTypeType.SuperType;

public class FnSemTypeSuper extends Pair<FnSemType, SuperType>
{
	public static final Set<FnSemTypeSuper> SET = new HashSet<>();

	public FnSemTypeSuper(final FnSemType semtype, final SuperType supersemtype)
	{
		super(semtype, supersemtype);
	}

	@Override
	public String toString()
	{
		return String.format("[SEMsuper semtypeid=%s supersemtypeid=%s]", this.first, this.second);
	}
}
