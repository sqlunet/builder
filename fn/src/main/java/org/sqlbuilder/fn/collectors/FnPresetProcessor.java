package org.sqlbuilder.fn.collectors;

import org.apache.xmlbeans.StringEnumAbstractBase;
import org.apache.xmlbeans.StringEnumAbstractBase.Table;
import org.sqlbuilder.common.Processor;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.fn.objects.LabelIType;
import org.sqlbuilder.fn.objects.CoreType;
import org.sqlbuilder.fn.objects.Pos;

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
		Progress.traceHeader("preset framenet tables", "poses coretypes");

		// pos
		for (final String pos : getPoses())
		{
			final Pos fnPos = new Pos(pos);
			Pos.SET.add(fnPos);
		}
		Progress.trace(1);

		// coretypes
		for (final String coretype : getCoreTypes())
		{
			final CoreType fnCoretype = new CoreType(coretype);
			CoreType.SET.add(fnCoretype);
		}
		Progress.trace(1);

		// labelitypes
		for (final String labelitype : getLabelITypes())
		{
			final LabelIType labelIType = new LabelIType(labelitype);
			LabelIType.SET.add(labelIType);
		}
		Progress.trace(1);

		Progress.traceTailer("preset framenet tables", "done");
	}

	public static String[] getValues(final Table types)
	{
		final String[] values = new String[types.lastInt()];
		for (int i = 1; i <= types.lastInt(); i++)
		{
			final StringEnumAbstractBase e = types.forInt(i);
			values[i - 1] = e.toString();
		}
		return values;
	}

	public static String[] getPoses()
	{
		return FnPresetProcessor.getValues(POSType.Enum.table);
	}

	public static String[] getCoreTypes()
	{
		return FnPresetProcessor.getValues(edu.berkeley.icsi.framenet.CoreType.Enum.table);
	}

	public static String[] getLabelITypes()
	{
		return FnPresetProcessor.getValues(LabelType.Itype.Enum.table);
	}

	public static void main(final String[] args)
	{
		System.out.println("\nPOSes:");
		for (final String s : FnPresetProcessor.getPoses())
		{
			System.out.println(s);
		}

		System.out.println("\nCORETYPEs:");
		for (final String s : FnPresetProcessor.getCoreTypes())
		{
			System.out.println(s);
		}

		System.out.println("\nLABELITYPEs:");
		for (final String s : FnPresetProcessor.getLabelITypes())
		{
			System.out.println(s);
		}
	}
}
