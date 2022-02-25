package org.sqlbuilder2.legacy;

import org.sqlbuilder.common.Insert;
import org.sqlbuilder.common.Processor;
import org.sqlbuilder2.ser.Serialize;
import org.sqlbuilder2.ser.Triplet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Index sense
 * From line in index.sense
 * To triplet(lemma,pos,offset)-to-sensekey map
 */
public class IndexSenseProcessor extends Processor
{
	private final String source;

	private final File inDir;

	private final File outDir;

	private final Properties conf;

	public IndexSenseProcessor(final Properties conf)
	{
		super("sk2nid");
		this.conf = conf;
		this.source = conf.getProperty("to_sensekeys.source");
		this.inDir = new File(conf.getProperty("to_sensekeys.home").replaceAll("\\$\\{source}", source));
		this.outDir = new File(conf.getProperty("legacy_serdir", "sers"));
		if (!this.outDir.exists())
		{
			//noinspection ResultOfMethodCallIgnored
			this.outDir.mkdirs();
		}
	}

	@Override
	public void run() throws IOException
	{
		run1();
	}

	public void run1() throws IOException
	{
		String inFile = conf.getProperty("to_sensekeys.map").replaceAll("\\$\\{source}", source);
		String outFile = Names.TO_SENSEKEYS.FILE.replaceAll("\\$\\{source}", source);

		var m = getLemmaPosOffsetToSensekey(new File(inDir, inFile));
		Serialize.serialize(m, new File(outDir, outFile + ".ser"));
		Insert.insert(m, new File(outDir, outFile + ".map"), conf.getProperty("to_sensekeys.table").replaceAll("\\$\\{source}", source), "word,pos,offset,sensekey", //
				r -> String.format("'%s','%s',%s,'%s'", r.getKey().first, r.getKey().second, r.getKey().third, r.getValue()));
	}

	private static Map<Triplet<String, Character, Integer>, String> getLemmaPosOffsetToSensekey(final File file) throws IOException
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
					.map(fields -> new SimpleEntry<>(new Triplet<>(getLemmaFromSensekey(fields[0]), getPosFromSensekey(fields[0]), Integer.parseInt(fields[1])), fields[0])) //
					.collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));
		}
	}

	private static Map<String, Integer> getSensekeyToOffset(final File file) throws IOException
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
					.map(fields -> new SimpleEntry<>(fields[0], Integer.parseInt(fields[1]))) //
					.collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));
		}
	}

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
}
