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
			final Values.Pos fnPos = new Values.Pos(pos);
			Values.Pos.SET.add(fnPos);
		}
		Progress.trace(1);

		// coretypes
		for (var coretype : getCoreTypes())
		{
			final Values.CoreType fnCoretype = new Values.CoreType(coretype);
			Values.CoreType.SET.add(fnCoretype);
		}
		Progress.trace(1);

		// labelitypes
		for (var labelitype : getLabelITypes())
		{
			final Values.LabelIType labelIType = new Values.LabelIType(labelitype);
			Values.LabelIType.SET.add(labelIType);
		}
		Progress.trace(1);

		Progress.traceTailer("preset framenet tables", "done");
	}

	public static String[] getValues(final Table types)
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

	public static void main(final String[] args)
	{
		System.out.println("\nPOSes:");
		for (final String s : getPoses())
		{
			System.out.println(s);
		}

		System.out.println("\nCORETYPEs:");
		for (final String s : getCoreTypes())
		{
			System.out.println(s);
		}

		System.out.println("\nLABELITYPEs:");
		for (final String s : getLabelITypes())
		{
			System.out.println(s);
		}
	}
}
