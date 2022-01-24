package org.sqlbuilder.fn.collectors;

import org.apache.xmlbeans.StringEnumAbstractBase.Table;
import org.sqlbuilder.common.Processor;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.fn.objects.Values;

import edu.berkeley.icsi.framenet.LabelType;
import edu.berkeley.icsi.framenet.POSType;

public class FnPresetProcessor extends Processor
{
	public FnPresetProcessor()
	{
		super("preset");
	}

	@Override
	public void run()
	{
		Progress.traceHeader("preset framenet tables", "poses coretypes labelitypes");

		// pos
		for (var pos : getPoses())
		{
			new Values.Pos(pos);
		}

		// coretypes
		for (var coretype : getCoreTypes())
		{
			new Values.CoreType(coretype);
		}

		// labelitypes
		for (var labelitype : getLabelITypes())
		{
			new Values.LabelIType(labelitype);
		}

		Progress.traceTailer("preset framenet tables", "done");
	}

	private static String[] getValues(final Table types)
	{
		final String[] values = new String[types.lastInt()];
		for (int i = 1; i <= types.lastInt(); i++)
		{
			final var e = types.forInt(i);
			values[i - 1] = String.format("%d,%s", i, e.toString());
		}
		return values;
	}

	public static String[] getPoses()
	{
		return getValues(POSType.Enum.table);
	}

	public static String[] getCoreTypes()
	{
		return getValues(edu.berkeley.icsi.framenet.CoreType.Enum.table);
	}

	public static String[] getLabelITypes()
	{
		return getValues(LabelType.Itype.Enum.table);
	}
}
