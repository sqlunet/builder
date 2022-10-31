package org.sqlbuilder.su;

import com.articulate.sigma.Formula;
import com.articulate.sigma.KB;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Kb extends KB implements Serializable
{
	private static final long serialVersionUID = 3120000480284537868L;

	private static final String[] CORE_FILES = new String[]{"Merge.kif", "Mid-level-ontology.kif", "english_format.kif"};

	private static final boolean silent = System.getProperties().containsKey("SILENT");

	private String[] filenames;

	public Kb(final String dirName)
	{
		super("SUMO", dirName);
	}

	public boolean make(final boolean full)
	{
		make(Kb.getFiles(this.kbDir, full));
		return true;
	}

	public boolean make(final String[] files)
	{
		this.filenames = files != null ? files : Kb.getFiles(this.kbDir, true);
		final String[] filePaths = new String[this.filenames.length];
		for (int i = 0; i < filePaths.length; i++)
		{
			filePaths[i] = this.kbDir + File.separatorChar + this.filenames[i];
		}
		Kb.makeKB(this, filePaths);
		return true;
	}

	private static void makeKB(final KB kb, final String[] filePaths)
	{
		for (final String filePath : filePaths)
		{
			System.out.println("\n" + filePath);
			kb.addConstituent(filePath);
		}
	}

	public boolean makeClausalForms()
	{
		long count = 0;
		for (Collection<Formula> fs : formulaIndex.values())
		{
			for (Formula f : fs)
			{
				/* Tuple.Triple<List<Clause>, Formula, Map<String, String>> cf = */
				f.getClausalForms();
				if (!silent)
				{
					if ((count++ % 100L) == 0)
					{
						System.out.println();
					}
					System.out.print('.');
				}
			}
		}
		return true;
	}

	protected static String[] getFiles(final String dirName, final boolean full)
	{
		if (full)
		{
			final List<String> list = new ArrayList<>(Arrays.asList(Kb.CORE_FILES));
			for (final String filename : Kb.getKifs(dirName))
			{
				if (list.contains(filename))
				{
					continue;
				}
				list.add(filename);
			}
			return list.toArray(new String[0]);
		}
		return Kb.CORE_FILES;
	}

	private static String[] getKifs(final String dirName)
	{
		final File file = new File(dirName);
		if (file.exists() && file.isDirectory())
		{
			return file.list((dir, name) -> name.endsWith(".kif"));
		}
		return new String[]{};
	}

	public String[] getFilenames()
	{
		return this.filenames;
	}
}
