package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.LexUnitDocument.LexUnit;

public class FnLexUnit extends FnLexUnitBase implements Insertable<FnLexUnit>
{
	public static final Set<FnLexUnit> SET = new HashSet<>();

	public final LexUnit lu;

	public FnLexUnit(final LexUnit lu)
	{
		super();
		this.lu = lu;
	}

	@Override
	public String dataRow()
	{
		final Definition definition = FnLexUnitBase.getDefinition(this.lu.getDefinition());

			// Long(1, this.lu.getID());
			// Long(2, this.lu.getFrameID());
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
			// Long(9, this.lu.getTotalAnnotated());
			return null;
	}

	@Override
	public String toString()
	{
		return String.format("[LU lu=%s frame=%s]", this.lu.getName(), this.lu.getFrame());
	}
}
