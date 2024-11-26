package org.sqlbuilder.fn;

import org.sqlbuilder.annotations.ProvidesIdTo;
import org.sqlbuilder.common.Names;
import org.sqlbuilder.fn.objects.FE;
import org.sqlbuilder.fn.objects.Frame;
import org.sqlbuilder.fn.objects.LexUnit;
import org.sqlbuilder.fn.objects.Word;
import org.sqlbuilder.fn.types.FeType;
import org.sqlbuilder2.ser.Pair;
import org.sqlbuilder2.ser.Serialize;
import org.sqlbuilder2.ser.Triplet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toMap;

public class Exporter
{
	private static final Comparator<Pair<String, String>> COMPARATOR = Comparator.comparing(Pair<String, String>::getFirst).thenComparing(Pair::getSecond);

	protected final Names names;

	protected final File outDir;

	public Exporter(final Properties conf)
	{
		this.names = new Names("fn");
		this.outDir = new File(conf.getProperty("fn_outdir_ser", "sers"));
		if (!this.outDir.exists())
		{
			//noinspection ResultOfMethodCallIgnored
			this.outDir.mkdirs();
		}
	}

	public void run() throws IOException
	{
		System.out.printf("%s %d%n", "frames", Frame.SET.size());
		System.out.printf("%s %d%n", "fes", FE.SET.size());
		System.out.printf("%s %d%n", "fetypes", StreamSupport.stream(Spliterators.spliteratorUnknownSize(FeType.COLLECTOR.iterator(), Spliterator.ORDERED), false).count());
		System.out.printf("%s %d%n", "lexunits", LexUnit.SET.size());
		System.out.printf("%s %d%n", "words", StreamSupport.stream(Spliterators.spliteratorUnknownSize(Word.COLLECTOR.iterator(), Spliterator.ORDERED), false).count());

		try (@ProvidesIdTo(type = Word.class) var ignored1 = Word.COLLECTOR.open(); //
		     @ProvidesIdTo(type = FeType.class) var ignored2 = FeType.COLLECTOR.open())
		{
			serialize();
			export();
		}
	}

	public void serialize() throws IOException
	{
		serializeFrames();
		serializeFEs();
		serializeLexUnits();
		serializeWords();
	}

	public void export() throws IOException
	{
		exportFrames();
		exportFEs();
		exportLexUnits();
		exportWords();
	}

	public void serializeFrames() throws IOException
	{
		var m = makeFramesMap();
		Serialize.serialize(m, new File(outDir, names.serFile("frames.resolve", "_[frame]-[frameid]")));
	}

	public void serializeFEs() throws IOException
	{
		var m = makeFEsMap();
		Serialize.serialize(m, new File(outDir, names.serFile("fes.resolve", "_[frame,fetype]-[feid,frameid,fetypeid]")));
	}

	public void serializeLexUnits() throws IOException
	{
		var m = makeLexUnitsMap();
		Serialize.serialize(m, new File(outDir, names.serFile("lexunits.resolve", "_[frame,lexunit]-[luid,frameid]")));
	}

	public void serializeWords() throws IOException
	{
		var m = makeWordMap();
		Serialize.serialize(m, new File(outDir, names.serFile("words.resolve", "_[word]-[fnwordid]")));
	}

	public void exportFrames() throws IOException
	{
		var m = makeFramesMap();
		export(m, new File(outDir, names.mapFile("frames.resolve", "_[frame]-[frameid]")));
	}

	public void exportFEs() throws IOException
	{
		var m = makeFEsTreeMap();
		export(m, new File(outDir, names.mapFile("fes.resolve", "_[frame,fetype]-[feid,frameid,fetypeid]")));
	}

	public void exportLexUnits() throws IOException
	{
		var m = makeLexUnitsTreeMap();
		export(m, new File(outDir, names.mapFile("lexunits.resolve", "_[frame,lexunit]-[luid,frameid]")));
	}

	public void exportWords() throws IOException
	{
		var m = makeWordMap();
		export(m, new File(outDir, names.mapFile("words.resolve", "_[word]-[fnwordid]")));
	}

	// M A P S

	/**
	 * Make word to wordid map
	 *
	 * @return word to wordid
	 */
	public Map<String, Integer> makeWordMap()
	{
		var stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(Word.COLLECTOR.iterator(), Spliterator.ORDERED), false);

		return stream //
				.map(w -> new SimpleEntry<>(w.getWord(), Word.COLLECTOR.apply(w))) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue, (x, r) -> x, TreeMap::new));
	}

	public Map<String, Integer> makeFramesMap()
	{
		return Frame.SET.stream() //
				.map(f -> new SimpleEntry<>(f.getName(), f.getID())) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue, (x, r) -> x, TreeMap::new));
	}

	public Map<Pair<String, String>, Triplet<Integer, Integer, Integer>> makeFEsMap()
	{
		var id2frame = Frame.SET.stream() //
				.map(f -> new SimpleEntry<>(f.getID(), f.getName())) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue));

		return FE.SET.stream() //
				.map(fe -> new SimpleEntry<>(new Pair<>(id2frame.get(fe.getFrameID()), fe.getName()), new Triplet<>(fe.getID(), fe.getFrameID(), FeType.getIntId(fe.getName())))) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue));
	}

	public Map<Pair<String, String>, Triplet<Integer, Integer, Integer>> makeFEsTreeMap()
	{
		var id2frame = Frame.SET.stream() //
				.map(f -> new SimpleEntry<>(f.getID(), f.getName())) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue));

		return FE.SET.stream() //
				//.peek(fe -> System.out.println(fe.getName() + " " + fe.getID()))
				.map(fe -> new SimpleEntry<>(new Pair<>(id2frame.get(fe.getFrameID()), fe.getName()), new Triplet<>(fe.getID(), fe.getFrameID(), FeType.getIntId(fe.getName())))) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue, (x, r) -> x, () -> new TreeMap<>(COMPARATOR)));
	}

	public Map<Pair<String, String>, Pair<Integer, Integer>> makeLexUnitsMap()
	{
		return LexUnit.SET.stream() //
				//.peek(lu->System.out.println(lu.getName() + " " + lu.getID()))
				.map(lu -> new SimpleEntry<>(new Pair<>(lu.getFrameName(), lu.getName()), new Pair<>(lu.getID(), lu.getFrameID()))) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue));
	}

	public Map<Pair<String, String>, Pair<Integer, Integer>> makeLexUnitsTreeMap()
	{
		return LexUnit.SET.stream() //
				//.peek(lu->System.out.println(lu.getName() + " " + lu.getID()))
				.map(lu -> new SimpleEntry<>(new Pair<>(lu.getFrameName(), lu.getName()), new Pair<>(lu.getID(), lu.getFrameID()))) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue, (x, r) -> x, () -> new TreeMap<>(COMPARATOR)));
	}

	public static <K, V> void export(Map<K, V> m, File file) throws IOException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file), true, StandardCharsets.UTF_8))
		{
			export(ps, m);
		}
	}

	public static <K, V> void export(PrintStream ps, Map<K, V> m)
	{
		m.forEach((strs, nids) -> ps.printf("%s -> %s%n", strs, nids));
	}
}
