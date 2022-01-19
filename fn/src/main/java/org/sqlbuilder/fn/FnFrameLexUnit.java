package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.FrameLUType;

public class FnFrameLexUnit extends FnLexUnitBase implements Insertable<FnLexUnitBase>
{
	public static final Set<FnFrameLexUnit> SET = new HashSet<>();

	public final long frameid;

	public final FrameLUType luType;

	public FnFrameLexUnit(final long frameid, final FrameLUType luType)
	{
		this.frameid = frameid;
		this.luType = luType;
	}

	@Override
	public String dataRow()
	{
		final Definition definition = FnLexUnitBase.getDefinition(this.luType.getDefinition());

		// Long(1, this.lu.getID());
		// Long(2, this.frame.getID());
		// String(3, this.lu.getName());
		// Int(4, this.lu.getPOS().intValue());
		if (definition.def != null)
		{
			// String(5, definition.def);
		}
		else
		{
			// Null(5, java.sql.Types.CHAR);
		}
		if (definition.dict != null)
		{
			// String(6, String.valueOf(definition.dict));
		}
		else
		{
			// Null(6, java.sql.Types.CHAR);
		}
		// String(7, this.lu.getStatus());
		// String(8, this.lu.getIncorporatedFE());
		// Long(9, this.lu.getSentenceCount().getAnnotated());
		return null;
	}

	@Override
	public String toString()
	{
		return String.format("[FRLU lutype=%s frameid=%s]", this.luType.getName(), this.frameid);
	}
}
