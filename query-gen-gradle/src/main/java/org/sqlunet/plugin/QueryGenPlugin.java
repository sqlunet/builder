package org.sqlunet.plugin;

import org.gradle.api.GradleException;
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
		System.out.println("variables: " + extension.variables);
		System.out.println("variables: " + extension.variablesExtra);
		System.out.println("outDir: " + extension.outDir);
		System.out.println("package: " + extension.qPackage);
		System.out.println("V: " + extension.v);
		String dest = new File(extension.outDir, extension.qPackage.replace(".", File.separator)).getAbsolutePath();
		String variablesPath = new File(extension.inDir, extension.variables).getAbsolutePath();
		String variablesExtraPath = new File(extension.inDir, extension.variablesExtra).getAbsolutePath();
		Generator.generateVClass(dest, extension.v, extension.qPackage, variablesPath, variablesExtraPath);
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
		System.out.println("variables: " + extension.variables);
		System.out.println("variables: " + extension.variablesExtra);
		System.out.println("outDir: " + extension.outDir);
		System.out.println("package: " + extension.qPackage);
		System.out.println("Q: " + extension.q);
		String dest = new File(extension.outDir, extension.qPackage.replace(".", File.separator)).getAbsolutePath();
		String factoryPath = new File(extension.inDir, extension.factory).getAbsolutePath();
		String variablesPath = new File(extension.inDir, extension.variables).getAbsolutePath();
		String variablesExtraPath = new File(extension.inDir, extension.variablesExtra).getAbsolutePath();
		Generator.generateQClass(factoryPath, dest, extension.q, extension.qPackage, variablesPath, variablesExtraPath);
	}

	void instantiate(Extension extension) throws Exception
	{
		System.out.println("outDir: " + extension.outDir);
		System.out.println("instantiate: " + extension.instantiate);
		System.out.println("variables: " + extension.variables);
		System.out.println("variables: " + extension.variablesExtra);
		System.out.println("instantiateDest: " + extension.instantiateDest);

		String dest = new File(extension.outDir, extension.instantiateDest.replace(".", File.separator)).getAbsolutePath();
		String sourcePath = new File(extension.inDir, extension.instantiate).getAbsolutePath();
		String variablesPath = new File(extension.inDir, extension.variables).getAbsolutePath();
		String variablesExtraPath = new File(extension.inDir, extension.variablesExtra).getAbsolutePath();
		Generator.instantiate(sourcePath, dest, extension.instantiate, variablesPath, variablesExtraPath);
	}
}