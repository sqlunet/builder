package org.sqlbuilder.pb;

public class PbNormalizer
{
	public static String normalize(final String str0)
	{
		String str = str0.trim();
		str = str.replace('\n', ' ');
		str = str.replaceAll("\\s\\s+", " ");
		return str;
	}
}
