package org.sqlbuilder.fn;

import org.junit.Test;

public class TestNormalizer
{
	@Test
	public void normalize1()
	{
		System.out.println("\n-- normalize ferealizations (feid)");
		new Normalizer("Fn_fes", "fetypeid", "feid") //
				.targets("Fn_ferealizations", "fetypeid", "feid") //
				.referenceThrough("Fn_lexunits", "luid", "Fn_frames", "frameid") //
				.dump();
	}

	@Test
	public void normalize2()
	{
		System.out.println("\n-- normalize fegrouprealizations (feid)");
		new Normalizer("Fn_fes", "fetypeid", "feid") //
				.targets("Fn_fegrouprealizations_fes", "fetypeid", "feid") //
				.referenceThrough("Fn_fegrouprealizations", "fegrid", "Fn_lexunits", "luid", "Fn_frames", "frameid") //
				.dump();
	}

	@Test
	public void normalize3()
	{
		System.out.println("\n-- normalize lexunits (incorporatedfeid)");
		new Normalizer("Fn_fes", "fetypeid", "feid") //
				.targets("Fn_lexunits", "incorporatedfetypeid", "incorporatedfeid") //
				.referenceThrough("Fn_frames", "frameid") //
				.dump();
	}

	@Test
	public void normalize4()
	{
		System.out.println("\n-- normalize grouppatterns_patterns (feid)");
		new Normalizer("Fn_fes", "fetypeid", "feid") //
				.targets("Fn_grouppatterns_patterns", "fetypeid", "feid") //
				.referenceThrough("Fn_patterns", "patternid", "Fn_fegrouprealizations", "fegrid", "Fn_lexunits", "luid") //
				.dump();
	}
}
