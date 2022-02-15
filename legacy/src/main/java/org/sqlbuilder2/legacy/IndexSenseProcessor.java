package org.sqlbuilder2.legacy;

import org.sqlbuilder2.ser.Serialize;
import org.sqlbuilder2.ser.Triplet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Index sense
 * From line in index.sense
 * To triplet(lemma,pos,offset)-to-sensekey map
 */
public class IndexSenseProcessor
{
	private static char getPosFromSensekey(final String sensekey)
	{
		int b = sensekey.indexOf('%');
		char c = sensekey.charAt(b + 1);
		switch (c)
		{
			case '1':
				return 'n';
			case '2':
				return 'v';
			case '3':
			case '5':
				return 'a';
			case '4':
				return 'r';
			default:
				throw new IllegalArgumentException(sensekey);
		}
	}

	private static String getLemmaFromSensekey(final String sensekey)
	{
		int b = sensekey.indexOf('%');
		return sensekey.substring(0, b).replace('_', ' ');
	}

	private static Map<Triplet<String, Character, Long>, String> processIndexSense(final File file) throws IOException
	{
		try (Stream<String> stream = Files.lines(file.toPath()))
		{
			/*
			abandon%2:31:00:: 00614057 5 3
			abandon%2:31:01:: 00613393 4 5
			abandon%2:38:00:: 02076676 3 6
			abandon%2:40:00:: 02228031 1 10
			abandon%2:40:01:: 02227741 2 6
			*/
			return stream //
					.filter(line -> !line.isEmpty() && line.charAt(0) != '#') //
					.map(line -> line.split("\\s")) //
					.map(fields -> new SimpleEntry<>(new Triplet<>(getLemmaFromSensekey(fields[0]), getPosFromSensekey(fields[0]), Long.parseLong(fields[1])), fields[0])) //
					.collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));
		}
	}

	public static void main(final String[] args) throws IOException
	{
		var m = processIndexSense(new File(args[0]));
		Serialize.serialize(m, new File(args[1]));
	}
}
