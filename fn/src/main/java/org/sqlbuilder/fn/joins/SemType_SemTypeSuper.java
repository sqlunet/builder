package org.sqlbuilder.fn.joins;

import org.sqlbuilder.fn.objects.SemType;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.SemTypeType.SuperType;

public class SemType_SemTypeSuper extends Pair<SemType, SuperType>
{
	public static final Set<SemType_SemTypeSuper> SET = new HashSet<>();

	public SemType_SemTypeSuper(final SemType semtype, final SuperType supersemtype)
	{
		super(semtype, supersemtype);
	}

	@Override
	public String toString()
	{
		return String.format("[SEMsuper semtypeid=%s supersemtypeid=%s]", this.first, this.second);
	}
}
