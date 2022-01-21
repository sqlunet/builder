package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.FnLexUnitBase;
import org.sqlbuilder.fn.HasID;

import java.util.HashSet;
import java.util.Set;

public class LexUnit extends FnLexUnitBase implements HasID, Insertable<LexUnit>
{
	public static final Set<LexUnit> SET = new HashSet<>();

	public final edu.berkeley.icsi.framenet.LexUnitDocument.LexUnit lu;

	public LexUnit(final edu.berkeley.icsi.framenet.LexUnitDocument.LexUnit lu)
	{
		super();
		this.lu = lu;
	}

	@Override
	public String dataRow()
	{
		final Definition definition = FnLexUnitBase.getDefinition(this.lu.getDefinition());

		return String.format("%d,'%s',%d,%s,%s,'%s',%s,%d,%d", //
				lu.getID(), //
				Utils.escape(lu.getName()), //
				lu.getPOS().intValue(), //
				Utils.nullableString(definition.def), //
				Utils.nullableChar(definition.dict), //
				lu.getStatus(), //
				Utils.nullableString(lu.getIncorporatedFE()), //
				lu.getTotalAnnotated(), //
				lu.getFrameID()); //
	}

	@Override
	public String toString()
	{
		return String.format("[LU luid=%d lu=%s frame=%s frameid=%d]", lu.getID(), lu.getName(), lu.getFrame(), lu.getFrameID());
	}
}
