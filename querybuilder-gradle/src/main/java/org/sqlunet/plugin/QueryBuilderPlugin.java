package org.sqlunet.plugin;

import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.sqlbuilder2.querybuilder.Generator;

import java.io.File;
import java.util.Arrays;

public class QueryBuilderPlugin implements Plugin<Project>
{
	@Override
	public void apply(Project project)
	{
		Extension extension = project.getExtensions().create("querybuilder_args", Extension.class);

		project.task("generateV").doLast(task -> {
			try
			{
				generateV(extension);
			}
			catch (Exception e)
			{
				//e.printStackTrace();
				throw new GradleException("While generating V", e);
			}
		});

		project.task("generateQV").doLast(task -> {
			try
			{
				generateQV(extension);
			}
			catch (Exception e)
			{
				//e.printStackTrace();
				throw new GradleException("While generating QV", e);
			}
		});

		project.task("generateQ").doLast(task -> {
			try
			{
				generateQ(extension);
			}
			catch (Exception e)
			{
				//e.printStackTrace();
				throw new GradleException("While generating Q", e);
			}
		});

		project.task("instantiate").doLast(task -> {
			try
			{
				instantiate(extension);
			}
			catch (Exception e)
			{
				//e.printStackTrace();
				throw new GradleException("While instantiating", e);
			}
		});
	}

	void generateV(Extension extension) throws Exception
	{
		System.out.println("inDir: " + extension.inDir);
		System.out.println("variables: " + Arrays.toString(extension.variables));
		System.out.println("outDir: " + extension.outDir);
		System.out.println("package: " + extension.qPackage);
		System.out.println("V: " + extension.v);
		String dest = new File(extension.outDir, extension.qPackage.replace(".", File.separator)).getAbsolutePath();
		String[] variablesPaths = Arrays.stream(extension.variables).map(f -> new File(extension.inDir, f).getAbsolutePath()).toArray(String[]::new);
		Generator.generateVClass(dest, extension.v, extension.qPackage, variablesPaths);
	}

	void generateQV(Extension extension) throws Exception
	{
		System.out.println("inDir: " + extension.inDir);
		System.out.println("factory: " + extension.factory);
		System.out.println("outDir: " + extension.outDir);
		System.out.println("package: " + extension.qPackage);
		System.out.println("QV: " + extension.qv);
		String dest = new File(extension.outDir, extension.qPackage.replace(".", File.separator)).getAbsolutePath();
		String factoryPath = new File(extension.inDir, extension.factory).getAbsolutePath();
		Generator.generateQVClass(factoryPath, dest, extension.qv, extension.qPackage);
	}

	void generateQ(Extension extension) throws Exception
	{
		System.out.println("inDir: " + extension.inDir);
		System.out.println("factory: " + extension.factory);
		System.out.println("variables: " + Arrays.toString(extension.variables));
		System.out.println("outDir: " + extension.outDir);
		System.out.println("package: " + extension.qPackage);
		System.out.println("Q: " + extension.q);
		String dest = new File(extension.outDir, extension.qPackage.replace(".", File.separator)).getAbsolutePath();
		String factoryPath = new File(extension.inDir, extension.factory).getAbsolutePath();
		String[] variablesPaths = Arrays.stream(extension.variables).map(f -> new File(extension.inDir, f).getAbsolutePath()).toArray(String[]::new);
		Generator.generateQClass(factoryPath, dest, extension.q, extension.qPackage, variablesPaths);
	}

	void instantiate(Extension extension) throws Exception
	{
		System.out.println("outDir: " + extension.outDir);
		System.out.println("instantiates: " + Arrays.toString(extension.instantiates));
		System.out.println("variables: " + Arrays.toString(extension.variables));
		System.out.println("instantiateDest: " + extension.instantiateDest);

		String dest = new File(extension.outDir, extension.instantiateDest.replace(".", File.separator)).getAbsolutePath();
		String[] sourcePaths = Arrays.stream(extension.instantiates).map(f -> new File(extension.inDir, f).getAbsolutePath()).toArray(String[]::new);
		String[] variablesPaths = Arrays.stream(extension.variables).map(f -> new File(extension.inDir, f).getAbsolutePath()).toArray(String[]::new);
		Generator.instantiate(sourcePaths, dest, variablesPaths);
	}
}