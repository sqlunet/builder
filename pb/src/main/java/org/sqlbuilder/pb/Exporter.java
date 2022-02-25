package org.sqlbuilder.pb;

import org.sqlbuilder.common.Names;
import org.sqlbuilder.common.ProvidesIdTo;
import org.sqlbuilder.pb.objects.Role;
import org.sqlbuilder.pb.objects.RoleSet;
import org.sqlbuilder.pb.objects.Theta;
import org.sqlbuilder.pb.objects.Word;
import org.sqlbuilder2.ser.Pair;
import org.sqlbuilder2.ser.Serialize;

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
import java.util.function.Function;

import static java.util.stream.Collectors.*;

public class Exporter
{
	//private static final Comparator<Pair<String, String>> NONSERIALIZABLE_COMPARATOR = Comparator.comparing(Pair<String, String>::getFirst).thenComparing(Pair::getSecond);
	//private static final Comparator<Pair<String, String>> COMPARATOR = (Comparator<Pair<String, String>> & Serializable) (p1, p2) -> NONSERIALIZABLE_COMPARATOR.compare(p1, p2);

	private static final Comparator<Pair<String, String>> COMPARATOR = Comparator.comparing(Pair<String, String>::getFirst).thenComparing(Pair::getSecond);

	protected final Names names;

	protected final File outDir;

	public Exporter(final Properties conf)
	{
		this.names = new Names("pb");
		this.outDir = new File(conf.getProperty("pb_outdir_ser", "sers"));
		if (!this.outDir.exists())
		{
			//noinspection ResultOfMethodCallIgnored
			this.outDir.mkdirs();
		}
	}

	public void run() throws IOException
	{
		System.out.printf("%s %d%n", "thetas", Theta.COLLECTOR.size());
		System.out.printf("%s %d%n", "roles", Role.COLLECTOR.size());
		System.out.printf("%s %d%n", "classes", RoleSet.COLLECTOR.size());
		System.out.printf("%s %d%n", "words", Word.COLLECTOR.size());
		duplicateRoles();

		try ( //
		      @ProvidesIdTo(type = Theta.class) var ignored1 = Theta.COLLECTOR.open(); //
		      @ProvidesIdTo(type = Role.class) var ignored2 = Role.COLLECTOR.open(); //
		      @ProvidesIdTo(type = RoleSet.class) var ignored3 = RoleSet.COLLECTOR.open(); //
		      @ProvidesIdTo(type = Word.class) var ignored4 = Word.COLLECTOR.open() //
		)
		{
			serialize();
			export();
		}
	}

	public void serialize() throws IOException
	{
		serializeThetas();
		serializeRoleSets();
		serializeRolesBare();
		serializeRoles();
		serializeWords();
	}

	public void export() throws IOException
	{
		exportThetas();
		exportRoleSets();
		exportRolesBare();
		exportRoles();
		exportWords();
	}

	public void serializeThetas() throws IOException
	{
		var m = makeThetasMap();
		Serialize.serialize(m, new File(outDir, names.serFile("thetas.resolve", "_[theta]-[thetaid]")));
	}

	public void serializeRoleSets() throws IOException
	{
		var m = makeRoleSetsMap();
		Serialize.serialize(m, new File(outDir, names.serFile("rolesets.resolve", "_[roleset]-[rolesetid]")));
	}

	public void serializeRolesBare() throws IOException
	{
		var m = makeRolesMap();
		Serialize.serialize(m, new File(outDir, names.serFile("roles.resolve", "_[roleset,argn]-[roleid]")));
	}

	private void serializeRoles() throws IOException
	{
		var m = makeRolesFromArgNToFullMap();
		Serialize.serialize(m, new File(outDir, names.serFile("roles.resolve", "_[roleset,argn]-[roleid,rolesetid]")));
	}

	public void serializeWords() throws IOException
	{
		var m = makeWordMap();
		Serialize.serialize(m, new File(outDir, names.serFile("words.resolve", "_[word]-[pbwordid]")));
	}

	public void exportThetas() throws IOException
	{
		var m = makeThetasMap();
		export(m, new File(outDir, names.mapFile("thetas.resolve", "_[theta]-[thetaid]")));
	}

	public void exportRoleSets() throws IOException
	{
		var m = makeRoleSetsMap();
		export(m, new File(outDir, names.mapFile("rolesets.resolve", "_[roleset]-[rolesetid]")));
	}

	public void exportRolesBare() throws IOException
	{
		var m = makeRolesTreeMap();
		export(m, new File(outDir, names.mapFile("roles.resolve", "_[roleset,argn]-[roleid]")));
	}

	public void exportRoles() throws IOException
	{
		var m = makeRolesFromArgNToFullTreeMap();
		export(m, new File(outDir, names.mapFile("roles.resolve", "_[roleset,argn]-[roleid,rolesetid]")));
	}

	public void exportWords() throws IOException
	{
		var m = makeWordMap();
		export(m, new File(outDir, names.mapFile("words.resolve", "_[word]-[pbwordid]")));
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

	void duplicateRoles()
	{
		Role.COLLECTOR.keySet().stream() //
				.map(r -> new Pair<>(r.getRoleSet().getName(), r.getArgn())) //
				.collect(groupingBy(Function.identity(), counting())) //
				.entrySet().stream() //
				.filter(e -> e.getValue() > 1).map(Map.Entry::getKey).forEach(p -> System.err.printf("%s is duplicated in %s%n", p.second, p.first));
	}

	// M A P S

	/**
	 * Make word to wordid map
	 *
	 * @return word to wordid
	 */
	public Map<String, Integer> makeWordMap()
	{
		return Word.COLLECTOR.entrySet().stream() //
				.map(e -> new SimpleEntry<>(e.getKey().getWord(), e.getValue())) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue, (x, r) -> x, TreeMap::new));
	}

	public Map<String, Integer> makeRoleSetsMap()
	{
		return RoleSet.COLLECTOR.entrySet().stream() //
				.map(e -> new SimpleEntry<>(e.getKey().getName(), e.getValue())) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue, (x, r) -> x, TreeMap::new));
	}

	public Map<String, Integer> makeThetasMap()
	{
		return Theta.COLLECTOR.entrySet().stream() //
				.map(e -> new SimpleEntry<>(e.getKey().getTheta(), e.getValue())) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue, (x, r) -> x, TreeMap::new));
	}

	public Map<Pair<String, String>, Integer> makeRolesMap()
	{
		return Role.COLLECTOR.entrySet().stream() //
				.map(e -> {
					var r = e.getKey(); //
					var rs = r.getRoleSet();
					return new Pair<>( //
							new Pair<>(rs.getName(), r.getArgn()), //
							e.getValue());
				}) //
				.collect(toMap(Pair::getFirst, Pair::getSecond, (x, r) -> {
					System.err.println("duplicate roleid for roleset: " + x + " / " + r);
					return x;
				}));
	}

	public Map<Pair<String, String>, Integer> makeRolesTreeMap()
	{
		return Role.COLLECTOR.entrySet().stream() //
				.map(e -> {
					var r = e.getKey(); //
					var rs = r.getRoleSet();
					return new Pair<>( //
							new Pair<>(rs.getName(), r.getArgn()), //
							e.getValue());
				}) //
				.collect(toMap(Pair::getFirst, Pair::getSecond, (x, r) -> {
					// System.err.println("duplicate roleid for roleset: " + x + " / " + r);
					return x;
				}, () -> new TreeMap<>(COMPARATOR)));
	}

	/**
	 * Detailed role maps
	 *
	 * @return (rolesetname, argn) -> (roleid, rolesetid)
	 */
	public Map<Pair<String, String>, Pair<Integer, Integer>> makeRolesFromArgNToFullMap()
	{
		return Role.COLLECTOR.entrySet().stream() //
				.map(e -> {
					var r = e.getKey(); //
					var rs = r.getRoleSet();
					return new Pair<>( //
							new Pair<>(rs.getName(), r.getArgn()), //
							new Pair<>(e.getValue(), rs.getIntId()));
				}) //
				.collect(toMap(Pair::getFirst, Pair::getSecond, (x, r) -> {
					// System.err.println("duplicate roleid for roleset: " + x + " / " + r);
					return x;
				}));
	}

	/**
	 * Detailed role maps
	 *
	 * @return (rolesetname, argn) -> (roleid, rolesetid)
	 */
	public Map<Pair<String, String>, Pair<Integer, Integer>> makeRolesFromArgNToFullTreeMap()
	{
		return Role.COLLECTOR.entrySet().stream() //
				.map(e -> {
					var r = e.getKey(); //
					var rs = r.getRoleSet();
					return new Pair<>( //
							new Pair<>(rs.getName(), r.getArgn()), //
							new Pair<>(e.getValue(), rs.getIntId()));
				}) //
				.collect(toMap(Pair::getFirst, Pair::getSecond, (x, r) -> {
					// System.err.println("duplicate roleid for roleset: " + x + " / " + r);
					return x;
				}, () -> new TreeMap<>(COMPARATOR)));
	}
}
