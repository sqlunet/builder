package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.LexUnitDocument.LexUnit;

public class FnLexUnit extends FnLexUnitBase implements HasID, Insertable<FnLexUnit>
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

		return String.format("%d,%d,'%s',%d,%s,%s,'%s',%s,%d", //
				lu.getID(), //
				lu.getFrameID(), //
				Utils.escape(lu.getName()), //
				lu.getPOS().intValue(), //
				Utils.nullableString(definition.def), //
				Utils.nullableChar(definition.dict), //
				lu.getStatus(), //
				Utils.nullableString(lu.getIncorporatedFE()), //
				lu.getTotalAnnotated());
	}

	@Override
	public String toString()
	{
		return String.format("[LU lu=%s  luid=%d frame=%s frameid=%d]", lu.getName(), lu.getID(), lu.getFrame(), lu.getFrameID());
	}
}
