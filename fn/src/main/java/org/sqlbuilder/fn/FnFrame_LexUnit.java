package org.sqlbuilder.fn;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.FrameLUType;

public class FnFrame_LexUnit extends FnLexUnitBase implements Insertable<FnLexUnitBase>
{
	public static final Set<FnFrame_LexUnit> SET = new HashSet<>();

	public final long frameid;

	public final FrameLUType lu;

	public FnFrame_LexUnit(final long frameid, final FrameLUType lu)
	{
		this.frameid = frameid;
		this.lu = lu;
	}

	@Override
	public String dataRow()
	{
		final Definition definition = FnLexUnitBase.getDefinition(this.lu.getDefinition());

		return String.format("%d,%d,'%s',%d,%s,%s,%s,%d", //
				lu.getID(), //
				frameid, //
				lu.getName(), //
				lu.getPOS().intValue(),  //
				Utils.nullableString(definition.def), //
				Utils.nullableChar(definition.dict), //
				Utils.nullableString(lu.getIncorporatedFE()), //
				lu.getSentenceCount().getAnnotated() //
		);

		// String(7, this.lu.getStatus());
	}

	@Override
	public String toString()
	{
		return String.format("[FR-LU lu=%s frameid=%s]", this.lu.getName(), this.frameid);
	}
}
