package org.sqlbuilder.fn.collectors;

import org.apache.xmlbeans.StringEnumAbstractBase.Table;
import org.sqlbuilder.common.Processor;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.annotations.ProvidesIdTo;
import org.sqlbuilder.fn.objects.Values;

import edu.berkeley.icsi.framenet.LabelType;
import edu.berkeley.icsi.framenet.POSType;

public class FnEnumCollector extends Processor
{
	public FnEnumCollector()
	{
		super("preset");
	}

	@Override
	public void run()
	{
		Progress.traceHeader("preset framenet tables", "poses coretypes labelitypes");
		makePoses();
		makeCoreTypes();
		makeLabelITypes();
		Progress.traceTailer(3);
	}

	@ProvidesIdTo(type = Values.Pos.class)
	private void makePoses()
	{
		int i = 1;
		for (var pos : getPoses())
		{
			Values.Pos.make(pos, i++);
		}
	}

	@ProvidesIdTo(type = Values.CoreType.class)
	private void makeCoreTypes()
	{
		int i = 1;
		for (var coretype : getCoreTypes())
		{
			Values.CoreType.make(coretype, i++);
		}
	}

	@ProvidesIdTo(type = Values.LabelIType.class)
	private void makeLabelITypes()
	{
		int i = 1;
		for (var labelitype : getLabelITypes())
		{
			Values.LabelIType.make(labelitype, i++);
		}
	}

	private static String[] getValues(final Table types)
	{
		final String[] values = new String[types.lastInt()];
		for (int i = 1; i <= types.lastInt(); i++)
		{
			final var e = types.forInt(i);
			values[i - 1] = e.toString();
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
