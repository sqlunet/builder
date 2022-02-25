package org.sqlbuilder.common;

import java.io.*;

/**
 * Deserializer
 */
public class DeSerialize
{
	/**
	 * Safe cast
	 *
	 * @param object object
	 * @param <T>    cast type
	 * @return cast object
	 */
	@SuppressWarnings("unchecked")
	private static <T> T safeCast(Object object)
	{
		return (T) object;
	}

	/**
	 * Deserialize
	 *
	 * @param inFile serialization file
	 * @param <T>    type of result
	 * @return deserialized object
	 * @throws IOException            io exception
	 * @throws ClassNotFoundException class not found exception
	 */
	public static <T> T deserialize(final File inFile) throws IOException, ClassNotFoundException
	{
		return safeCast(deSerialize(inFile));
	}

	/**
	 * Deserialize object from file
	 *
	 * @param inFile input file
	 * @return object
	 * @throws IOException            io exception
	 * @throws ClassNotFoundException class not found exception
	 */
	public static Object deSerialize(final File inFile) throws IOException, ClassNotFoundException
	{
		try (InputStream is = new FileInputStream(inFile))
		{
			return deSerialize(is);
		}
	}

	/**
	 * Deserialize object from input stream
	 *
	 * @param is input stream
	 * @return object
	 * @throws IOException            io exception
	 * @throws ClassNotFoundException class not found exception
	 */
	public static Object deSerialize(final InputStream is) throws IOException, ClassNotFoundException
	{
		try (ObjectInputStream ois = new ObjectInputStream(is))
		{
			return ois.readObject();
		}
	}
}
