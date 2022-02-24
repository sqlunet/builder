package org.sqlbuilder.fn;

import org.junit.BeforeClass;
import org.junit.Test;
import org.sqlbuilder.fn.collectors.FnEnumCollector;
import org.sqlbuilder.fn.objects.Values;

public class Presets
{
	@BeforeClass
	public static void init()
	{
		new FnEnumCollector().run();
	}

	@Test
	public void presetsPoses()
	{
		System.out.println("\nPOSes:");
		for (final String s : FnEnumCollector.getCoreTypes())
		{
			System.out.println(s);
		}
	}

	@Test
	public void presetsPosDataRows()
	{
		Values.Pos.MAP.forEach((key, value) -> System.out.printf("%d,%s%n", value, key.dataRow()));
	}

	@Test
	public void presetsCoreTypes()
	{
		System.out.println("\nCORETYPEs:");
		for (final String s : FnEnumCollector.getCoreTypes())
		{
			System.out.println(s);
		}
	}

	@Test
	public void presetsCoreTypeDataRows()
	{
		Values.CoreType.MAP.forEach((key, value) -> System.out.printf("%d,%s%n", value, key.dataRow()));
	}

	@Test
	public void presetsITypes()
	{
		System.out.println("\nLABELITYPEs:");
		for (final String s : FnEnumCollector.getLabelITypes())
		{
			System.out.println(s);
		}
	}

	@Test
	public void presetsITypeDataRows()
	{
		Values.LabelIType.MAP.forEach((key, value) -> System.out.printf("%d,%s%n", value, key.dataRow()));
	}
}
