package org.sqlbuilder2.ser;

import java.io.*;

public class Serialize
{
	/**
	 * Serialize object to file
	 *
	 * @param object  object
	 * @param outFile output file
	 * @throws IOException io exception
	 */
	static public void serialize(final Object object, final File outFile) throws IOException
	{
		try (OutputStream os = new FileOutputStream(outFile))
		{
			serialize(os, object);
		}
	}

	/**
	 * Serialize object to output stream
	 *
	 * @param os     output stream
	 * @param object object
	 * @throws IOException io exception
	 */
	private static void serialize(final OutputStream os, final Object object) throws IOException
	{
		try (ObjectOutputStream oos = new ObjectOutputStream(os))
		{
			oos.writeObject(object);
		}
	}
}
