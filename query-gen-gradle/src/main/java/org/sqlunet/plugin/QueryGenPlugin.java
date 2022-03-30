package org.sqlunet.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.io.File;

public class QueryGenPlugin implements Plugin<Project>
{
	@Override
	public void apply(Project project)
	{
		Extension extension = project.getExtensions().create("querygen_args", Extension.class);

		project.task("generateV").doLast(task -> {
			generateV(extension);
		});

		project.task("generateQV").doLast(task -> {
			try
			{
				generateQV(extension);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});

		project.task("generateQ").doLast(task -> {
			generateQ(extension);
		});
	}

	void generateV(Extension extension)
	{
		System.out.println("outDir: " + extension.outDir);
		System.out.println("variables: " + extension.variables);
		System.out.println("V: " + extension.v);
		File dir = new File(extension.outDir);
		if (!dir.exists())
		{
			dir.mkdirs();
		}

	}

	void generateQV(Extension extension) throws Exception
	{
		System.out.println("outDir: " + extension.outDir);
		System.out.println("factory: " + extension.factory);
		System.out.println("QV: " + extension.qv);
		File dir = new File(extension.outDir);
		if (!dir.exists())
		{
			dir.mkdirs();
		}
		new Runner().run(extension.factory);
	}

	void generateQ(Extension extension)
	{
		System.out.println("outDir: " + extension.outDir);
		System.out.println("factory: " + extension.factory);
		System.out.println("variables: " + extension.variables);
		System.out.println("Q: " + extension.q);

		File dir = new File(extension.outDir);
		if (!dir.exists())
		{
			dir.mkdirs();
		}
	}
}