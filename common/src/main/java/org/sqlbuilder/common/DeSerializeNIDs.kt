/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.sqlbuilder.common;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Deserialize ID to Numeric IDs maps
 */
public class DeSerializeNIDs
{
	static final String NID_PREFIX = "nid_";

	static final String WORDS_FILE = "words";

	static final String SENSES_FILE = "senses";

	static final String SYNSETS_FILE = "synsets";

	static final String SER_EXTENSION = ".ser";

	/**
	 * Deserialize id-to_nid maps
	 *
	 * @param inDir input directory
	 * @return id-to-nid map indexed by name
	 * @throws IOException            io exception
	 * @throws ClassNotFoundException class not found exception
	 */
	public static Map<String, Map<String, Integer>> deserializeAllNIDs(final File inDir) throws IOException, ClassNotFoundException
	{
		Map<String, Map<String, Integer>> maps = new HashMap<>();
		try (InputStream is = new FileInputStream(new File(inDir, NID_PREFIX + WORDS_FILE + SER_EXTENSION)))
		{
			var m = DeSerializeNIDs.deSerializeNIDs(is);
			maps.put(WORDS_FILE, m);
		}
		try (InputStream is = new FileInputStream(new File(inDir, NID_PREFIX + SENSES_FILE + SER_EXTENSION)))
		{
			var m = DeSerializeNIDs.deSerializeNIDs(is);
			maps.put(SENSES_FILE, m);
		}
		try (InputStream is = new FileInputStream(new File(inDir, NID_PREFIX + SYNSETS_FILE + SER_EXTENSION)))
		{
			var m = DeSerializeNIDs.deSerializeNIDs(is);
			maps.put(SYNSETS_FILE, m);
		}
		return maps;
	}

	/**
	 * Deserialize id-to_nid maps
	 *
	 * @param inFile input file
	 * @return id-to-nid map indexed by name
	 * @throws IOException            io exception
	 * @throws ClassNotFoundException class not found exception
	 */
	public static Map<String, Integer> deserializeNIDs(final File inFile) throws IOException, ClassNotFoundException
	{
		try (InputStream is = new FileInputStream(inFile))
		{
			return DeSerializeNIDs.deSerializeNIDs(is);
		}
	}

	/**
	 * Deserialize id-to_nid map
	 *
	 * @param is input stream
	 * @return id-to-nid map
	 * @throws IOException            io exception
	 * @throws ClassNotFoundException class not found exception
	 */
	public static Map<String, Integer> deSerializeNIDs(final InputStream is) throws IOException, ClassNotFoundException
	{
		//noinspection unchecked
		return (Map<String, Integer>) deSerialize(is);
	}

	/**
	 * Deserialize object
	 *
	 * @param is input stream
	 * @return object
	 * @throws IOException            io exception
	 * @throws ClassNotFoundException class not found exception
	 */
	private static Object deSerialize(final InputStream is) throws IOException, ClassNotFoundException
	{
		try (ObjectInputStream ois = new ObjectInputStream(is))
		{
			return ois.readObject();
		}
	}

	/**
	 * Main
	 *
	 * @param args command-line arguments
	 * @throws IOException            io exception
	 * @throws ClassNotFoundException class not found exception
	 */
	static public void main(String[] args) throws IOException, ClassNotFoundException
	{
		File inDir = new File(args[0]);
		if (!inDir.isDirectory())
		{
			System.exit(1);
		}

		Map<String, Map<String, Integer>> maps = deserializeAllNIDs(inDir);
		System.out.printf("%s %d%n", WORDS_FILE, maps.get(WORDS_FILE).size());
		System.out.println(maps.get(WORDS_FILE).entrySet().iterator().next().getClass());
		System.out.printf("%s %d%n", SENSES_FILE, maps.get(SENSES_FILE).size());
		System.out.println(maps.get(SENSES_FILE).entrySet().iterator().next().getClass());
		System.out.printf("%s %d%n", SYNSETS_FILE, maps.get(SYNSETS_FILE).size());
		System.out.println(maps.get(SYNSETS_FILE).entrySet().iterator().next().getClass());
	}
}
