package org.sqlbuilder.fn;

import org.junit.Test;
import org.sqlbuilder.fn.collectors.FnPresetProcessor;

public class Presets
{
	@Test
	public void presets()
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
