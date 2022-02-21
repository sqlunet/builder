package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.FnModule;
import org.sqlbuilder.common.HasID;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.fn.types.FeType;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import edu.berkeley.icsi.framenet.FrameLUType;
import edu.berkeley.icsi.framenet.LexUnitDocument;

public class LexUnit implements HasID, Insertable
{
	public static final Set<LexUnit> SET = new HashSet<>();

	private final int luid;

	private final String name;

	private final int pos;

	private final int frameid;

	private final String frameName;

	private final String definition;

	private final Character dict;

	private final String incorporatedFE;

	private final int totalAnnotated;

	public static LexUnit make(final LexUnitDocument.LexUnit lu)
	{
		var u = new LexUnit(lu);

		boolean isNew = SET.add(u);
		if (!isNew)
		{
			Logger.instance.logWarn(FnModule.MODULE_ID, lu.getDomNode().getOwnerDocument().getDocumentURI(), "lu-duplicate", u.toString());
		}
		return u;
	}

	public static LexUnit make(final FrameLUType lu, final int frameid, final String frameName)
	{
		var u = new LexUnit(lu, frameid, frameName);

		boolean isNew = SET.add(u);
		if (!isNew)
		{
			Logger.instance.logWarn(FnModule.MODULE_ID, lu.getDomNode().getOwnerDocument().getDocumentURI(), "lu-duplicate", u.toString());
		}
		return u;
	}

	private LexUnit(final LexUnitDocument.LexUnit lu)
	{
		this.luid = lu.getID();
		this.name = lu.getName();
		this.pos = lu.getPOS().intValue();
		final Definition def = Definition.getDefinition(lu.getDefinition());
		this.definition = def.def;
		this.dict = def.dict;
		this.incorporatedFE = lu.getIncorporatedFE();
		this.totalAnnotated = lu.getTotalAnnotated();
		this.frameid = lu.getFrameID();
		this.frameName = lu.getFrame();
	}

	private LexUnit(final FrameLUType lu, final int frameid, final String frameName)
	{
		this.luid = lu.getID();
		this.name = lu.getName();
		this.pos = lu.getPOS().intValue();
		final Definition def = Definition.getDefinition(lu.getDefinition());
		this.definition = def.def;
		this.dict = def.dict;
		this.incorporatedFE = lu.getIncorporatedFE();
		this.totalAnnotated = 0;
		this.frameid = frameid;
		this.frameName = frameName;
		// this.lemmaId = lu.getLemmaID();
	}

	// A C C E S S

	public int getID()
	{
		return luid;
	}

	public String getName()
	{
		return name;
	}

	public String getFrameName()
	{
		return frameName;
	}

	public int getFrameID()
	{
		return frameid;
	}

	// I D E N T I T Y

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		LexUnit that = (LexUnit) o;
		return luid == that.luid;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(luid);
	}

	// O R D E R

	public static Comparator<LexUnit> COMPARATOR = Comparator.comparing(LexUnit::getName).thenComparing(LexUnit::getID);

	// I N S E R T

	@RequiresIdFrom(type = FeType.class)
	@Override
	public String dataRow()
	{
		// luid,lexunit,posid,ludefinition,ludict,incorporatedfetypeid,totalannotated,frameid
		return String.format("%d,'%s',%d,%s,%s,%s,%d,%d", //
				luid, //
				Utils.escape(name), //
				pos, //
				Utils.nullableQuotedEscapedString(definition), //
				Utils.nullableQuotedChar(dict), //
				FeType.getSqlId(incorporatedFE), //
				totalAnnotated, //
				frameid); //
	}

	@Override
	public String comment()
	{
		return String.format("frame=%s,incfe=%s", frameName, incorporatedFE);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("[LU luid=%d lu=%s frameid=%d frame=%s]", luid, name, frameid, frameName);
	}

	// D E F I N I T I O N

	public static class Definition
	{
		public final Character dict;

		public final String def;

		public Definition(final Character dict, final String definition)
		{
			super();
			this.dict = dict;
			this.def = definition;
		}

		public static Definition getDefinition(final String definition0)
		{
			Character dict = null;
			String definition = definition0;
			if (definition0.startsWith("COD"))
			{
				dict = 'O';
				definition = definition0.substring(3);
			}
			if (definition0.startsWith("FN"))
			{
				dict = 'F';
				definition = definition0.substring(2);
			}
			// noinspection ConstantConditions
			if (definition != null)
			{
				definition = definition.replaceAll("[ \t\n.:]*$|^[ \t\n.:]*", "");
			}
			return new Definition(dict, definition);
		}

		@Override
		public String toString()
		{
			return this.dict + "|<" + this.def + ">";
		}
	}
}
