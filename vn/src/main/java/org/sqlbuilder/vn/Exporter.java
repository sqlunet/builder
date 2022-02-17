package org.sqlbuilder.vn;

import org.sqlbuilder.common.Names;
import org.sqlbuilder.common.ProvidesIdTo;
import org.sqlbuilder.vn.joins.Class_Role;
import org.sqlbuilder.vn.objects.Role;
import org.sqlbuilder.vn.objects.RoleType;
import org.sqlbuilder.vn.objects.VnClass;
import org.sqlbuilder2.ser.DeSerialize;
import org.sqlbuilder2.ser.Pair;
import org.sqlbuilder2.ser.Serialize;
import org.sqlbuilder2.ser.Triplet;

import java.io.*;
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
		System.err.printf("%s %d%n", "roletypes", RoleType.COLLECTOR.size());
		System.err.printf("%s %d%n", "roles", Role.COLLECTOR.size());
		System.err.printf("%s %d%n", "classes", VnClass.COLLECTOR.size());
		System.err.printf("%s %d%n", "classes_roles", Class_Role.SET.size());

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
		serializeRoles();
		serializeRoleTypes();
		serializeClassesRoles();
		serializeClassTagsRoles();
	}

	public void export() throws IOException
	{
		exportClasses();
		exportClassTags();
		exportRoles();
		exportRoleTypes();
		exportClassTagsRoles();
		exportClassesRoles();
	}

	public void serializeClassTags() throws IOException
	{
		var m = makeClassTagsMap();
		Serialize.serialize(m, new File(outDir, names.serFile("classes.resolve", "_by_tag")));
	}

	public void serializeClasses() throws IOException
	{
		var m = makeClassesMap();
		Serialize.serialize(m, new File(outDir,names.serFile("classes.resolve", "_by_name")));
	}

	public void serializeRoles() throws IOException
	{
		var m = makeRolesMap();
		Serialize.serialize(m, new File(outDir, names.serFile("roles.resolve")));
	}

	public void serializeRoleTypes() throws IOException
	{
		var m = makeRoleTypesMap();
		Serialize.serialize(m, new File(outDir, names.serFile("roletypes.resolve")));
	}

	public void serializeClassTagsRoles() throws IOException
	{
		var m = makeClassTagsRolesMap();
		Serialize.serialize(m, new File(outDir, names.serFile("classes_roles.resolve", "_by_tag")));
	}

	public void serializeClassesRoles() throws IOException
	{
		var m = makeClassesRolesMap();
		Serialize.serialize(m, new File(outDir, names.serFile("classes_roles.resolve", "_by_name")));
	}

	public void exportClassTags() throws IOException
	{
		var m = makeClassTagsMap();
		export(m, new File(outDir, names.mapFile("classes.resolve","_by_tag")));
	}

	public void exportClasses() throws IOException
	{
		var m = makeClassesMap();
		export(m, new File(outDir, names.mapFile("classes.resolve", "_by_name")));
	}

	public void exportRoles() throws IOException
	{
		var m = makeRolesMap();
		export(m, new File(outDir, names.mapFile("roles.resolve")));
	}

	public void exportRoleTypes() throws IOException
	{
		var m = makeRoleTypesMap();
		export(m, new File(outDir, names.mapFile("roletypes.resolve")));
	}

	public void exportClassTagsRoles() throws IOException
	{
		var m = makeClassTagsRolesTreeMap();
		export(m, new File(outDir, names.mapFile("classes_roles.resolve","_by_tag")));
	}

	public void exportClassesRoles() throws IOException
	{
		var m = makeClassesRolesTreeMap();
		export(m, new File(outDir, names.mapFile("classes_roles.resolve", "_by_name")));
	}

	public Map<String, Integer> makeClassesMap()
	{
		return VnClass.COLLECTOR.entrySet().stream() //
				.map(e -> new SimpleEntry<>(e.getKey().getName(), e.getValue())) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue, (x, r) -> x, TreeMap::new));
	}

	public Map<String, Integer> makeClassTagsMap()
	{
		return VnClass.COLLECTOR.entrySet().stream() //
				.map(e -> new SimpleEntry<>(e.getKey().getName(), e.getValue())) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue, (x, r) -> x, TreeMap::new));
	}

	public Map<String, Integer> makeRolesMap()
	{
		return Role.COLLECTOR.entrySet().stream() //
				.map(e -> new SimpleEntry<>(e.getKey().toString(), e.getValue())) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue, (x, r) -> x, TreeMap::new));
	}

	public Map<String, Integer> makeRoleTypesMap()
	{
		return RoleType.COLLECTOR.entrySet().stream() //
				.map(e -> new SimpleEntry<>(e.getKey().getType(), e.getValue())) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue, (x, r) -> x, TreeMap::new));
	}

	public Map<Pair<String, String>, Triplet<Integer, Integer, Integer>> makeClassTagsRolesMap()
	{
		return Class_Role.SET.stream() //
				.sorted(Class_Role.COMPARATOR) //
				.map(p -> new Pair<>( //
						new Pair<>(p.getClazz().getTag(), p.getRole().getRoleType().getType()), //
						new Triplet<>(p.getClazz().getIntId(), p.getRole().getIntId(), p.getRole().getRoleType().getIntId()))) //
				.collect(toMap(Pair::getFirst, Pair::getSecond));
	}

	public Map<Pair<String, String>, Triplet<Integer, Integer, Integer>> makeClassTagsRolesTreeMap()
	{
		return Class_Role.SET.stream() //
				.sorted(Class_Role.COMPARATOR) //
				.map(p -> new Pair<>( //
						new Pair<>(p.getClazz().getTag(), p.getRole().getRoleType().getType()), //
						new Triplet<>(p.getClazz().getIntId(), p.getRole().getIntId(), p.getRole().getRoleType().getIntId()))) //
				.collect(toMap(Pair::getFirst, Pair::getSecond, (existing, replacement) -> {
					throw new RuntimeException(existing + " > " + replacement);
				}, () -> new TreeMap<>(COMPARATOR)));
	}

	public Map<Pair<String, String>, Triplet<Integer, Integer, Integer>> makeClassesRolesMap()
	{
		return Class_Role.SET.stream() //
				.sorted(Class_Role.COMPARATOR) //
				.map(p -> new Pair<>( //
						new Pair<>(p.getClazz().getName(), p.getRole().getRoleType().getType()), //
						new Triplet<>(p.getClazz().getIntId(), p.getRole().getIntId(), p.getRole().getRoleType().getIntId()))) //
				.collect(toMap(Pair::getFirst, Pair::getSecond));
	}

	public Map<Pair<String, String>, Triplet<Integer, Integer, Integer>> makeClassesRolesTreeMap()
	{
		return Class_Role.SET.stream() //
				.sorted(Class_Role.COMPARATOR) //
				.map(p -> new Pair<>( //
						new Pair<>(p.getClazz().getName(), p.getRole().getRoleType().getType()), //
						new Triplet<>(p.getClazz().getIntId(), p.getRole().getIntId(), p.getRole().getRoleType().getIntId()))) //
				.collect(toMap(Pair::getFirst, Pair::getSecond, (existing, replacement) -> {
					throw new RuntimeException(existing + " > " + replacement);
				}, () -> new TreeMap<>(COMPARATOR)));
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
