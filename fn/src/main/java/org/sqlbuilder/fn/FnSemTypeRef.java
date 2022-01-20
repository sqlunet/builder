package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.SemTypeRefType;

public class FnSemTypeRef implements HasID, Insertable<FnSemTypeRef>
{
	public static final Set<FnSemTypeRef> SET = new HashSet<>();

	public final SemTypeRefType ref;

	public FnSemTypeRef(final SemTypeRefType ref)
	{
		this.ref = ref;
	}

	@Override
	public String dataRow()
	{
		// Long(1, this.semtype.getID());
		// String(2, this.semtype.getName());
		// String(3, this.semtype.getAbbrev());
		// String(4, this.semtype.getDefinition());
		return null;
	}

	@Override
	public String toString()
	{
		return String.format("[SEM semtypeid=%s name=%s]", this.ref.getID(), this.ref.getName());
	}
}
