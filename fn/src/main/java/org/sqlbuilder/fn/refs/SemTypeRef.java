package org.sqlbuilder.fn.refs;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.HasID;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.SemTypeRefType;

public class SemTypeRef implements HasID, Insertable<SemTypeRef>
{
	public static final Set<SemTypeRef> SET = new HashSet<>();

	public final SemTypeRefType ref;

	public SemTypeRef(final SemTypeRefType ref)
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
