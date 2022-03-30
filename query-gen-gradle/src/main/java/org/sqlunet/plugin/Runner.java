package org.sqlunet.plugin;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Runner
{
	public static void main(String... args) throws Exception
	{
		new Runner().run(args[0]);
	}

	private static String readCode(String sourcePath) throws FileNotFoundException
	{
		InputStream stream = new FileInputStream(sourcePath);
		String separator = System.getProperty("line.separator");
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		return reader.lines().collect(Collectors.joining(separator));
	}

	private static Path saveSource(String source) throws IOException
	{
		String tmpProperty = System.getProperty("java.io.tmpdir");
		Path sourcePath = Paths.get(tmpProperty, "Factory.java");
		Files.write(sourcePath, source.getBytes(UTF_8));
		return sourcePath;
	}

	private static Path compileSource(Path javaFile)
	{
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		compiler.run(null, null, null, javaFile.toFile().getAbsolutePath());
		return javaFile.getParent().resolve("Factory.class");
	}

	private static Class<?> loadClass(Path javaClass) throws MalformedURLException, ClassNotFoundException
	{
		URL classUrl = javaClass.getParent().toFile().toURI().toURL();
		URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{classUrl});
		return Class.forName("Factory", true, classLoader);
	}

	private static Object instantiateClass(Class<?> clazz) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException
	{
		return clazz.getDeclaredConstructor().newInstance();
	}

	private static Object instantiateClass(String sourcePath) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, IOException, ClassNotFoundException
	{
		String source = readCode(sourcePath);
		Path javaFile = saveSource(source);
		Path classFile = compileSource(javaFile);
		Class<?> clazz = loadClass(classFile);
		Object factory = instantiateClass(clazz);
		return clazz.getDeclaredConstructor().newInstance();
	}

	@SuppressWarnings("unchecked")
	public static <T> T safeCast(Object object)
	{
		return (T) object;
	}

	public static Pair<Supplier<String[]>,Function<String,String[]>> build(String sourcePath) throws Exception
	{
		Object factory = instantiateClass(sourcePath);
		Function<String, String[]> f = safeCast(factory);
		Supplier<String[]> s = safeCast(factory);
		return new Pair<>(s,f);
	}

	public static void run(String sourcePath) throws Exception
	{
		var factory = build(sourcePath);
		var keys = factory.first.get();
		for (var key : keys)
		{
			var vals = factory.second.apply(key);
			System.out.printf("%s -> %s%n", key, Arrays.toString(vals));
		}
	}
}
