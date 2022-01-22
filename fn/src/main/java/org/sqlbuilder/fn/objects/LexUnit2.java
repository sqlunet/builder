package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.FnModule;

import java.util.HashSet;
import java.util.Set;

import edu.berkeley.icsi.framenet.FrameLUType;

public class LexUnit2 implements Insertable<LexUnit>
{
	public static final Set<LexUnit2> SET = new HashSet<>();

	public final long frameid;

	public final FrameLUType lu;

	public LexUnit2(final long frameid, final FrameLUType lu)
	{
		this.lu = lu;
		this.frameid = frameid;
		final boolean isNew = SET.add(this);
		if (!isNew)
		{
			Logger.instance.logWarn(FnModule.MODULE_ID, "LexUnit2", "frame-lu-duplicate", null, -1, null, toString());
		}
	}

	@Override
	public String dataRow()
	{
		final Definition definition = Definition.getDefinition(this.lu.getDefinition());

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
