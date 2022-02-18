package org.sqlbuilder.fn;

import org.sqlbuilder.common.Names;
import org.sqlbuilder.fn.objects.Frame;
import org.sqlbuilder2.ser.Serialize;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import static java.util.stream.Collectors.toMap;

public class Exporter
{
	protected final Names names;

	protected File outDir;

	public Exporter(final Properties conf)
	{
		this.names = new Names("fn");
		this.outDir = new File(conf.getProperty("fn_outdir_ser", "sers"));
		if (!this.outDir.exists())
		{
			this.outDir.mkdirs();
		}
	}

	public void run() throws IOException
	{
		System.err.printf("%s %d%n", "frames", Frame.SET.size());
		serialize();
		export();
	}

	public void serialize() throws IOException
	{
		serializeFrames();
	}

	public void export() throws IOException
	{
		exportFrames();
	}

	public void serializeFrames() throws IOException
	{
		var m = makeFramesMap();
		Serialize.serialize(m, new File(outDir, names.serFile("frames.resolve", "_by_name")));
	}

	public void exportFrames() throws IOException
	{
		var m = makeFramesMap();
		export(m, new File(outDir, names.mapFile("frames.resolve", "_by_name")));
	}

	public Map<String, Integer> makeFramesMap()
	{
		return Frame.SET.stream() //
				.map(f -> new SimpleEntry<>(f.getName(), f.getID())) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue, (x, r) -> x, TreeMap::new));
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
