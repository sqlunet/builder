package org.sqlbuilder.sumo;

public class KBLoader
{
	public static Kb kb;

	public static String getPath()
	{
		String kbPath = System.getProperty("sumopath");
		if (kbPath == null)
		{
			kbPath = System.getenv("SUMOHOME");
		}
		assert kbPath != null;
		return kbPath;
	}

	public static final String[] ALL_FILES = null;

	public static final String[] CORE_FILES = {"Merge.kif", "Mid-level-ontology.kif"};

	public static final String[] SAMPLE_FILES = {"Merge.kif", "Mid-level-ontology.kif", "Communications.kif"};

	public static String[] getScope()
	{
		String scope = System.getProperties().getProperty("SCOPE", "all");
		switch (scope)
		{
			case "all":
				return ALL_FILES;
			case "core":
				return CORE_FILES;
			case "samples":
				return SAMPLE_FILES;
			default:
				return scope.split("\\s");
		}
	}

	public static Kb loadKb()
	{
		return loadKb(getScope());
	}

	public static Kb loadKb(final String[] files)
	{
		String kbPath = getPath();
		Kb kb = new Kb(kbPath);
		System.out.printf("Kb building%n");
		boolean result = kb.make(files);
		assert result;
		System.out.printf("%nKb built%n");
		return kb;
	}

	public void load()
	{
		kb = loadKb();
	}
}
