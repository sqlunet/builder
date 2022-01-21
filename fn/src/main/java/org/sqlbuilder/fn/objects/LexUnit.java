package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.HasID;

import java.util.HashSet;
import java.util.Set;
import edu.berkeley.icsi.framenet.LexUnitDocument;

public class LexUnit extends LexUnitBase implements HasID, Insertable<LexUnit>
{
	public static final Set<LexUnit> SET = new HashSet<>();

	public final LexUnitDocument.LexUnit lu;

	public LexUnit(final LexUnitDocument.LexUnit lu)
	{
		super();
		this.lu = lu;
	}

	@Override
	public String dataRow()
	{
		final Definition definition = LexUnitBase.getDefinition(this.lu.getDefinition());

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
