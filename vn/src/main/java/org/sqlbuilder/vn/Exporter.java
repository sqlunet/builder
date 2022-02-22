package org.sqlbuilder.vn;

import org.sqlbuilder.common.Names;
import org.sqlbuilder.common.ProvidesIdTo;
import org.sqlbuilder.vn.objects.Role;
import org.sqlbuilder.vn.objects.RoleType;
import org.sqlbuilder.vn.objects.VnClass;
import org.sqlbuilder.vn.objects.Word;
import org.sqlbuilder2.ser.Pair;
import org.sqlbuilder2.ser.Serialize;
import org.sqlbuilder2.ser.Triplet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import static java.util.stream.Collectors.toMap;

public class Exporter
{
	//private static final Comparator<Pair<String, String>> NONSERIALIZABLE_COMPARATOR = Comparator.comparing(Pair<String, String>::getFirst).thenComparing(Pair::getSecond);
	//private static final Comparator<Pair<String, String>> COMPARATOR = (Comparator<Pair<String, String>> & Serializable) (p1, p2) -> NONSERIALIZABLE_COMPARATOR.compare(p1, p2);

	private static final Comparator<Pair<String, String>> COMPARATOR = Comparator.comparing(Pair<String, String>::getFirst).thenComparing(Pair::getSecond);

	protected final Names names;

	protected File outDir;

	public Exporter(final Properties conf)
	{
		this.names = new Names("vn");
		this.outDir = new File(conf.getProperty("vn_outdir_ser", "sers"));
		if (!this.outDir.exists())
		{
			this.outDir.mkdirs();
		}
	}

	public void run() throws IOException
	{
		System.err.printf("%s %d%n", "classes", VnClass.COLLECTOR.size());
		System.err.printf("%s %d%n", "roles", Role.COLLECTOR.size());
		System.err.printf("%s %d%n", "roletypes", RoleType.COLLECTOR.size());
		System.err.printf("%s %d%n", "words", Word.COLLECTOR.size());

		try ( //
		      @ProvidesIdTo(type = RoleType.class) var ignored1 = RoleType.COLLECTOR.open(); //
		      @ProvidesIdTo(type = Role.class) var ignored2 = Role.COLLECTOR.open(); //
		      @ProvidesIdTo(type = VnClass.class) var ignored3 = VnClass.COLLECTOR.open(); //
		)
		{
			serialize();
			export();
		}
	}

	public void serialize() throws IOException
	{
		serializeClasses();
		serializeClassTags();
		serializeRoleTypes();
		serializeRolesUsingClassTags();
		serializeRolesUsingClassNames();
		serializeWords();
	}

	public void export() throws IOException
	{
		exportClasses();
		exportClassTags();
		exportRoleTypes();
		exportRolesUsingClassTags();
		exportRolesUsingClassNames();
		exportWords();
	}

	public void serializeClassTags() throws IOException
	{
		var m = makeClassTagsMap();
		Serialize.serialize(m, new File(outDir, names.serFile("classes.resolve", "_[classtag]-[classid]")));
	}

	public void serializeClasses() throws IOException
	{
		var m = makeClassesMap();
		Serialize.serialize(m, new File(outDir, names.serFile("classes.resolve", "_[classname]-[classid]")));
	}

	public void serializeRoleTypes() throws IOException
	{
		var m = makeRoleTypesMap();
		Serialize.serialize(m, new File(outDir, names.serFile("roletypes.resolve", "_[roletype]-[roletypeid]")));
	}

	public void serializeRolesUsingClassTags() throws IOException
	{
		var m = makeClassTagsRolesMap();
		Serialize.serialize(m, new File(outDir, names.serFile("roles.resolve", "_[classtag,roletype]-[roleid,classid,roletypeid]")));
	}

	public void serializeRolesUsingClassNames() throws IOException
	{
		var m = makeClassesRolesMap();
		Serialize.serialize(m, new File(outDir, names.serFile("roles.resolve", "_[classname,roletype]-[roleid,classid,roletypeid]")));
	}

	public void serializeWords() throws IOException
	{
		Serialize.serialize(Word.COLLECTOR.toHashMap(), new File(outDir, names.mapFile("words.resolve", "_[word]-[vnwordid]")));
	}

	public void exportClassTags() throws IOException
	{
		var m = makeClassTagsMap();
		export(m, new File(outDir, names.mapFile("classes.resolve", "_[classtag]-[classid]")));
	}

	public void exportClasses() throws IOException
	{
		var m = makeClassesMap();
		export(m, new File(outDir, names.mapFile("classes.resolve", "_[classname]-[classid]")));
	}

	public void exportRoleTypes() throws IOException
	{
		var m = makeRoleTypesMap();
		export(m, new File(outDir, names.mapFile("roletypes.resolve", "_[roletype]-[roletypeid]")));
	}

	public void exportRolesUsingClassTags() throws IOException
	{
		var m = makeClassTagsRolesTreeMap();
		export(m, new File(outDir, names.mapFile("roles.resolve", "_[classtag,roletype]-[roleid,classid,roletypeid]")));
	}

	public void exportRolesUsingClassNames() throws IOException
	{
		var m = makeClassesRolesTreeMap();
		export(m, new File(outDir, names.mapFile("roles.resolve", "_[classname,roletype]-[roleid,classid,roletypeid]")));
	}

	public void exportWords() throws IOException
	{
		export(Word.COLLECTOR, new File(outDir, names.mapFile("words.resolve", "_[word]-[vnwordid]")));
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

	// M A P S

	/**
	 * Make classname to classid map
	 *
	 * @return classname to classid
	 */
	public Map<String, Integer> makeClassesMap()
	{
		return VnClass.COLLECTOR.entrySet().stream() //
				.map(e -> new SimpleEntry<>(e.getKey().getName(), e.getValue())) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue, (x, r) -> x, TreeMap::new));
	}

	/**
	 * Make classtags to classid map
	 *
	 * @return classtags to classid
	 */

	public Map<String, Integer> makeClassTagsMap()
	{
		return VnClass.COLLECTOR.entrySet().stream() //
				.map(e -> new SimpleEntry<>(e.getKey().getTag(), e.getValue())) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue, (x, r) -> x, TreeMap::new));
	}

	/**
	 * Make roletype to roletypeid map
	 *
	 * @return roletype to roletypeid map
	 */
	public Map<String, Integer> makeRoleTypesMap()
	{
		return RoleType.COLLECTOR.entrySet().stream() //
				.map(e -> new SimpleEntry<>(e.getKey().getType(), e.getValue())) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue, (x, r) -> x, TreeMap::new));
	}

	/**
	 * Make classtag,roletype to ids map
	 *
	 * @return (classtag, roletype) to (roleid,classid,roletypeid)
	 */
	public Map<Pair<String, String>, Triplet<Integer, Integer, Integer>> makeClassTagsRolesMap()
	{
		return Role.COLLECTOR.keySet().stream() //
				.map(p -> new Pair<>( //
						new Pair<>(p.getClazz().getTag(), p.getRestrRole().getRoleType().getType()), //
						new Triplet<>(p.getIntId(), p.getClazz().getIntId(), p.getRestrRole().getRoleType().getIntId()))) //
				.collect(toMap(Pair::getFirst, Pair::getSecond));
	}

	/**
	 * Make classtag,roletype to ids treemap
	 *
	 * @return (classtag, roletype) to (roleid,classid,roletypeid)
	 */
	public Map<Pair<String, String>, Triplet<Integer, Integer, Integer>> makeClassTagsRolesTreeMap()
	{
		return Role.COLLECTOR.keySet().stream() //
				.map(p -> new Pair<>( //
						new Pair<>(p.getClazz().getTag(), p.getRestrRole().getRoleType().getType()), //
						new Triplet<>(p.getIntId(), p.getClazz().getIntId(), p.getRestrRole().getRoleType().getIntId()))) //
				.collect(toMap(Pair::getFirst, Pair::getSecond, (existing, replacement) -> {
					throw new RuntimeException(existing + " > " + replacement);
				}, () -> new TreeMap<>(COMPARATOR)));
	}

	/**
	 * Make classname,roletype to ids map
	 *
	 * @return (classnameroletype) to (roleid,classid,roletypeid)
	 */
	public Map<Pair<String, String>, Triplet<Integer, Integer, Integer>> makeClassesRolesMap()
	{
		return Role.COLLECTOR.keySet().stream() //
				.map(r -> new Pair<>( //
						new Pair<>(r.getClazz().getName(), r.getRestrRole().getRoleType().getType()), //
						new Triplet<>(r.getIntId(), r.getClazz().getIntId(), r.getRestrRole().getRoleType().getIntId()))) //
				.collect(toMap(Pair::getFirst, Pair::getSecond));
	}

	/**
	 * Make classname,roletype to ids treemap
	 *
	 * @return (classname, roletype) to (roleid,classid,roletypeid)
	 */
	public Map<Pair<String, String>, Triplet<Integer, Integer, Integer>> makeClassesRolesTreeMap()
	{
		return Role.COLLECTOR.keySet().stream() //
				.map(r -> new Pair<>( //
						new Pair<>(r.getClazz().getName(), r.getRestrRole().getRoleType().getType()), //
						new Triplet<>(r.getIntId(), r.getClazz().getIntId(), r.getRestrRole().getRoleType().getIntId()))) //
				.collect(toMap(Pair::getFirst, Pair::getSecond, (existing, replacement) -> {
					throw new RuntimeException(existing + " > " + replacement);
				}, () -> new TreeMap<>(COMPARATOR)));
	}
}
