package org.sqlunet.plugin;

public class Extension
{
	// input
	public String inDir = ".";
	public String variables = "Names.properties";
	public String variablesExtra = "NamesExtra.properties";
	public String factory = "Factory.java";
	public String instantiate = "SqLiteDialect.java";
	// ouput
	public String outDir = "${buildDir}/generated/main/java";
	public String v = "V";
	public String q = "Q";
	public String qv = "QV";
	public String qPackage = "org.sqlunet";
	public String instantiateDest = "org.sqlunet.sql";
}
